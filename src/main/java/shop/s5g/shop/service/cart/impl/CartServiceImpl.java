package shop.s5g.shop.service.cart.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.shop.dto.cart.request.CartBookSelectRequestDto;
import shop.s5g.shop.dto.cart.request.CartSessionStorageDto;
import shop.s5g.shop.dto.cart.response.CartBooksResponseDto;
import shop.s5g.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.cart.Cart;
import shop.s5g.shop.entity.cart.CartPk;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.ResourceNotFoundException;

import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.cart.CartFieldValue;
import shop.s5g.shop.repository.cart.CartRedisRepository;
import shop.s5g.shop.repository.cart.CartRepository;


import shop.s5g.shop.repository.delivery.DeliveryFeeRepository;
import shop.s5g.shop.service.cart.CartService;
import shop.s5g.shop.service.member.MemberService;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartRedisRepository cartRedisRepository;
    private final MemberService memberService;
    private final BookRepository bookRepository;
    private final DeliveryFeeRepository deliveryFeeRepository;


    // 회원아이디를 이용해 Mysql 에 저장되어있는 Cart 리스트반환
    @Transactional(readOnly = true)
    @Override
    public List<Cart> getBooksInDbByCustomerId(String customerLoginId) {
        Member member = memberService.getMember(customerLoginId);

        return cartRepository.findAllByCartPk_CustomerId(member.getId());
    }

    // 로그인 했을 때 세션스토리지와 db에 있는 걸 합쳐서 레디스에 옮김
    @Transactional
    @Override
    public void saveMergedCartToRedis(String customerLoginId,List<CartBookInfoRequestDto> cartBookInfoListInSession) {

        // 정상로그아웃을 했을 경우에는 해당회원의 레디스 loginFlag 를 삭제한다.
        // 근데 만약 loginFlag 가 존재한다면 비정상로그아웃으로 간주하고 db에 있는 걸 안 가져오고 레디스에 세션 스토리지 내용만 추가하는 방법 고려
        if (customerLoginId.isBlank()) {
            throw new BadRequestException("CustomerLoginId Is Not Valid");
        }

        if (Objects.isNull(getLoginFlag(customerLoginId))) {    // null 이라는 것은 정상 로드아웃이 되었다는 것
            List<Cart> booksInDb = getBooksInDbByCustomerId(customerLoginId);

            // db에 있는 걸 레디스로 저장
            booksInDb.forEach(cart -> {
                cartRedisRepository.putBook(cart.getBook().getBookId(), cart.getQuantity(),
                    customerLoginId);
            });
            setLoginFlag(customerLoginId);
        }


        // 세션 스토리지에 있는 걸 레디스로 저장
        cartBookInfoListInSession.forEach(cartBookInfo ->{
            cartRedisRepository.putBook(cartBookInfo.bookId(), cartBookInfo.quantity(),
                customerLoginId);
        });
    }

    @Override
    public int getCartCountInRedis(String customerLoginId){
        return cartRedisRepository.getBooksInRedisCart(customerLoginId).size();
    }

    // 로그아웃할 때 레디스에 있는 걸 db 에 저장한다.
    @Transactional
    @Override
    public void FromRedisToDb(String customerLoginId) {


        if (customerLoginId.isBlank()) {
            throw new BadRequestException("CustomerLoginId Is Not Valid");
        }


        Member member = memberService.getMember(customerLoginId);
        Map<Long, Integer> booksInRedisCart = getBooksInRedisCart(customerLoginId);
        List<Cart> booksInDbCart = new ArrayList<>();


        booksInRedisCart.forEach((bookId,quantity) ->{

            bookRepository.findById(bookId).ifPresent(book -> {
                booksInDbCart.add(
                    new Cart(new CartPk(member.getId(), bookId), book, member, quantity));
            });
        });

        cartRepository.deleteAllByCartPk_CustomerId(member.getId());
        saveAll(booksInDbCart);
        deleteOldCart(customerLoginId);
        deleteLoginFlag(customerLoginId);
    }


    @Transactional
    @Override
    public void saveAll(List<Cart> mergedCart) {
        cartRepository.saveAll(mergedCart);
    }



    @Transactional
    @Override
    public void controlQuantity(Long bookId, int change, String customerLoginId) {

        if (customerLoginId.isBlank()) {
            throw new BadRequestException("CustomerLoginId Is Not Valid");
        }

        if (change > 0) {
            putBook(bookId, 1, customerLoginId);
        } else {
            reduceBookQuantity(bookId, customerLoginId);
        }
    }

    @Transactional
    @Override
    public void putBook(Long bookId, Integer quantity, String customerLoginId) {

        if (customerLoginId.isBlank()) {
            throw new BadRequestException("CustomerLoginId Is Not Valid");
        }

        if (bookRepository.findById(bookId).isPresent()) {
            cartRedisRepository.putBook(bookId, quantity, customerLoginId);
        }else {
            throw new ResourceNotFoundException("해당 도서를 담는데 실패했습니다.");
        }
    }


    @Transactional
    @Override
    public void reduceBookQuantity(Long bookId, String customerLoginId) {

        cartRedisRepository.reduceBookQuantity(bookId, customerLoginId);
    }


    @Transactional
    @Override
    public void deleteBookFromCart(Long bookId, String customerLoginId) {

        if (customerLoginId.isBlank()) {
            throw new BadRequestException("CustomerLoginId Is Not Valid");
        }

        cartRedisRepository.deleteBookFromCart(bookId, customerLoginId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CartBooksResponseDto> lookUpAllBooks(String customerLoginId) {

        if (customerLoginId.isBlank()) {
            throw new BadRequestException("SessionId Is Not Valid");
        }

        Map<Long, CartFieldValue> booksInRedisCart = getBooksInRedisCartWithStatus(
            customerLoginId);


        if (booksInRedisCart.isEmpty()) {
            List<CartBooksResponseDto> emptyList = new ArrayList<>();
            return emptyList;
        }

        List<Book> booksInfoInRedisCart = bookRepository.findAllById(
            booksInRedisCart.keySet().stream().toList());

        List<CartBooksResponseDto> cartBooks = booksInfoInRedisCart.stream()
            .map(book -> new CartBooksResponseDto(book.getBookId(), book.getPrice(),
                BigDecimal.valueOf(book.getPrice())
                    .multiply(BigDecimal.valueOf(1).subtract(book.getDiscountRate())),
                booksInRedisCart.get(book.getBookId()).getQuantity(), book.getStock(),
                book.getTitle(), "image", booksInRedisCart.get(book.getBookId()).isStatus())
            ).toList();

        return cartBooks;
    }


    @Transactional(readOnly = true)
    @Override
    public List<CartBooksResponseDto> lookUpAllBooksWhenGuest(CartSessionStorageDto cartSessionStorageDto) {


        Map<Long, Integer> booksInSessionStorage = cartSessionStorageDto.cartBookInfoList().stream().collect(
            Collectors.toMap(CartBookInfoRequestDto::bookId, CartBookInfoRequestDto::quantity,Integer::sum));

        if (booksInSessionStorage.isEmpty()) {
            List<CartBooksResponseDto> emptyList = new ArrayList<>();
            return emptyList;
        }

        List<Book> booksInfo = bookRepository.findAllById(booksInSessionStorage.keySet());

        List<CartBooksResponseDto> cartBooks = booksInfo.stream()
            .map(book -> new CartBooksResponseDto(book.getBookId(), book.getPrice(),
                BigDecimal.valueOf(book.getPrice())
                    .multiply(BigDecimal.valueOf(1).subtract(book.getDiscountRate())),
                booksInSessionStorage.get(book.getBookId()), book.getStock(), book.getTitle(),
                "image", true)
            ).toList();

        return cartBooks;
    }


    //TODO 이후 배달비 관련 테이블이 완성되면 배달비랑 조건 수정필요
    @Override
    public CartDetailInfoResponseDto getTotalPriceAndDeliverFee(
        List<CartBooksResponseDto> cartBooks) {

        BigDecimal totalPrice = cartBooks.stream().map(cartBook -> cartBook.discountedPrice()
                .multiply(BigDecimal.valueOf(cartBook.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long deliveryFee = 3000;
        long freeShippingThreshold = 30000;
        return new CartDetailInfoResponseDto(totalPrice, deliveryFee, freeShippingThreshold);
    }



    @Override
    public void setLoginFlag(String customerLoginId) {
        cartRedisRepository.setLoginFlag(customerLoginId);
    }

    @Override
    public Boolean getLoginFlag(String customerLoginId) {
        return cartRedisRepository.getLoginFlag(customerLoginId);
    }

    @Override
    public void deleteLoginFlag(String customerLoginId) {
        cartRedisRepository.deleteLoginFlag(customerLoginId);
    }

    @Override
    public Map<Long, Integer> getBooksInRedisCart(String customerLoginId) {
        Map<Long, Integer> result = new HashMap<>();

        Map<Object, Object> booksInRedisCart = cartRedisRepository.getBooksInRedisCart(customerLoginId);

        booksInRedisCart.entrySet()
            .forEach(entry -> result.put((Long) entry.getKey(), ((CartFieldValue) entry.getValue()).getQuantity()));

        return result;

    }

    @Override
    public Map<Long, CartFieldValue> getBooksInRedisCartWithStatus(String customerLoginId) {
        Map<Object, Object> booksInRedisCart = cartRedisRepository.getBooksInRedisCart(
            customerLoginId);

        return booksInRedisCart.entrySet().stream().collect(Collectors.toMap(
            entry -> (Long) entry.getKey(),
            entry -> (CartFieldValue)  entry.getValue()
        ));
    }

    @Override
    public Map<Long, Integer> getBooksInRedisCartWithStatusTrue(String customerLoginId) {


        Map<Object, Object> booksInRedisCart = cartRedisRepository.getBooksInRedisCart(customerLoginId);

        return booksInRedisCart.entrySet().stream().filter(
                entry -> entry.getValue() instanceof CartFieldValue
                    && ((CartFieldValue) entry.getValue()).isStatus())
            .collect(Collectors.toMap(
                entry -> (Long) entry.getKey(),
                entry -> ((CartFieldValue) entry.getValue()).getQuantity()
            ));



    }



    @Override
    public void deleteOldCart(String customerLoginId) {
        cartRedisRepository.deleteOldCart(customerLoginId);
    }

    @Transactional
    public void removeAccount(String customerLoginId) {

        if (customerLoginId.isBlank()) {
            throw new BadRequestException("SessionId Is Not Valid");
        }

        deleteOldCart(customerLoginId);
        cartRepository.deleteAllByCartPk_CustomerId(
            memberService.getMember(customerLoginId).getId());
        deleteLoginFlag(customerLoginId);

    }

    @Transactional
    @Override
    public List<CartBookInfoRequestDto> getBooksWhenPurchase(String customerLoginId) {

        if (customerLoginId.isBlank()) {
            throw new BadRequestException("SessionId Is Not Valid");
        }

        Map<Long, Integer> booksInRedisCart = getBooksInRedisCartWithStatusTrue(customerLoginId);

        List<CartBookInfoRequestDto> books = new ArrayList<>();

        booksInRedisCart.forEach((bookId,quantity) ->{
            books.add(new CartBookInfoRequestDto(bookId, quantity));
        });

        return books;
    }

    @Transactional
    @Override
    public void changeBookStatus(String customerLoginId,
        CartBookSelectRequestDto cartBookSelectRequestDto) {

        if (customerLoginId.isBlank()) {
            throw new BadRequestException("SessionId Is Not Valid");
        }

        cartRedisRepository.changeBookStatus(customerLoginId, cartBookSelectRequestDto);

    }

}
