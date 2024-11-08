package shop.s5g.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.cart.request.CartControlQuantityRequestDto;
import shop.s5g.shop.dto.cart.request.CartDeleteBookRequestDto;
import shop.s5g.shop.dto.cart.request.CartLoginRequestDto;
import shop.s5g.shop.dto.cart.request.CartPutRequestDto;
import shop.s5g.shop.dto.cart.request.CartSessionStorageDto;
import shop.s5g.shop.dto.cart.response.CartBooksResponseDto;
import shop.s5g.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.cart.CartService;

@ConditionalOnBean(RedisConfig.class)
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class CartController {

    private final CartService cartService;
    //담기
    @PostMapping("/cart")
    public ResponseEntity<MessageDto> putBook(@RequestBody @Validated CartPutRequestDto cartPutRequestDto,
        BindingResult bindingResult,@AuthenticationPrincipal ShopMemberDetail shopMemberDetail) {
        String customerLoginId = shopMemberDetail.getLoginId();

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Field Error When Putting Book In Cart");
        }

        cartService.putBook(cartPutRequestDto.bookId(), cartPutRequestDto.quantity(),
            customerLoginId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("장바구니에 물품이 담겼습니다."));
    }

    // 조회 (장바구니상세페이지 접근)
    @GetMapping("/cart")
    public ResponseEntity<Map<String,Object>> lookUpAllBooks(@AuthenticationPrincipal ShopMemberDetail shopMemberDetail) {

        String customerLoginId = shopMemberDetail.getLoginId();

        List<CartBooksResponseDto> cartBooks = cartService.lookUpAllBooks(customerLoginId);
        CartDetailInfoResponseDto cartTotalPriceAndDeliverFee = cartService.getTotalPriceAndDeliverFee(
            cartBooks);
        Map<String, Object> response = new HashMap<>();
        response.put("books", cartBooks);
        response.put("feeInfo", cartTotalPriceAndDeliverFee);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cart/guest")
    public ResponseEntity<Map<String, Object>> lookUpAllBooksWhenGuest(
        @RequestParam(value = "cartBookInfoList", required = false) String cartSessionStorage) throws JsonProcessingException {

        byte[] decoded = Base64.getUrlDecoder()
            .decode(URLDecoder.decode(cartSessionStorage, StandardCharsets.UTF_8));
        String decodedCart = new String(decoded, StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        CartSessionStorageDto cartSessionStorageDto = objectMapper.readValue(decodedCart,
            CartSessionStorageDto.class);

        List<CartBooksResponseDto> cartBooks = cartService.lookUpAllBooksWhenGuest(
            cartSessionStorageDto);

        CartDetailInfoResponseDto cartTotalPriceAndDeliverFee = cartService.getTotalPriceAndDeliverFee(
            cartBooks);
        Map<String, Object> response = new HashMap<>();
        response.put("books", cartBooks);
        response.put("feeInfo", cartTotalPriceAndDeliverFee);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 장바구니 상세페이지에서 +,- 버튼으로 수량조절
    @PatchMapping("/cart/controlQuantity")
    public ResponseEntity<Void> controlQuantity(@RequestBody @Validated
    CartControlQuantityRequestDto controlQuantityReqDto, BindingResult bindingResult,
        @AuthenticationPrincipal ShopMemberDetail shopMemberDetail) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Field Error When Controlling Book Quantity In Cart");
        }

        String customerLoginId = shopMemberDetail.getLoginId();

        cartService.controlQuantity(controlQuantityReqDto.bookId(), controlQuantityReqDto.change(),
            customerLoginId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // 특정도서 전체삭제
    @DeleteMapping("/cart")
    public ResponseEntity<MessageDto> deleteBookInCart(
        @RequestBody @Validated CartDeleteBookRequestDto deleteBookReq,
        BindingResult bindingResult, @AuthenticationPrincipal ShopMemberDetail shopMemberDetail) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Field Error When Deleting Book In Cart");
        }
        String customerLoginId = shopMemberDetail.getLoginId();

        cartService.deleteBookFromCart(deleteBookReq.bookId(), customerLoginId);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageDto("선택상품을 삭제했습니다."));
    }

    // 로그인 했을 때 세션스토리지와 db를 합쳐 레디스에 저장할 컨트롤러
    @PostMapping("/cart/login")
    public ResponseEntity<Void> mergedCartToRedis(
        @RequestBody @Validated CartLoginRequestDto cartLoginRequestDto,
        BindingResult bindingResult,@AuthenticationPrincipal ShopMemberDetail shopMemberDetail) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Field Error When Converting Cart From SessionStorage To Redis");
        }
        String customerLoginId = shopMemberDetail.getLoginId();

        cartService.saveMergedCartToRedis(customerLoginId, cartLoginRequestDto.cartBookInfoList());

        return ResponseEntity.status(HttpStatus.OK).build();
    }






}
