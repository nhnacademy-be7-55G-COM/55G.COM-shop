package shop.S5G.shop.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.S5G.shop.config.RedisConfig;
import shop.S5G.shop.dto.cart.request.CartDeleteBookRequestDto;
import shop.S5G.shop.dto.cart.request.CartPutRequestDto;
import shop.S5G.shop.dto.cart.request.CartReduceBookQuantityRequestDto;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.service.cart.CartService;

@ConditionalOnBean(RedisConfig.class)
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class CartController {

    private final CartService cartService;

    //담기
    @PostMapping("/cart")
    public ResponseEntity<Void> putBook(@RequestBody @Validated CartPutRequestDto cartPutRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Field Error When Putting Book In Cart");
        }

        cartService.putBook(cartPutRequestDto.bookId(), cartPutRequestDto.quantity(),
            cartPutRequestDto.sessionId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 조회
    @GetMapping("/cart/{sessionId}")
    public ResponseEntity<List<CartBooksResponseDto>> lookUpAllBooks(
        @PathVariable("sessionId") String sessionId) {
        List<CartBooksResponseDto> cartBooks = cartService.lookUpAllBooks(sessionId);

        return new ResponseEntity<>(cartBooks, HttpStatus.OK);
    }

    // 도서 1개 삭제
    @PatchMapping("/cart")
    public ResponseEntity<Void> reduceBookQuantityInCart(@RequestBody @Validated
    CartReduceBookQuantityRequestDto reduceQuantityReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Field Error When Reducing Book Quantity In Cart");
        }

        cartService.reduceBookQuantity(reduceQuantityReq.bookId(), reduceQuantityReq.sessionId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 특정도서 전체삭제
    @DeleteMapping("/cart")
    public ResponseEntity<Void> deleteBookInCart(
        @RequestBody @Validated CartDeleteBookRequestDto deleteBookReq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Field Error When Deleting Book In Cart");
        }

        cartService.deleteBookFromCart(deleteBookReq.bookId(), deleteBookReq.sessionId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // AuthenticationSuccessEvent (인증을 성공했으나 아직 세션이 새롭게 생성되지 않고 이전의 세션을 가지고 있는 시점)
    @PostMapping("/cart/RedisToDb/{sessionId}/{customerLoginId}")
    public ResponseEntity<Void> SaveFromRedisToDb(@PathVariable("sessionId") String sessionId,
        @PathVariable("customerLoginId") String customerLoginId) {

        cartService.saveMergedCartToDb(sessionId, customerLoginId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // SessionFixationProtectionEvent (인증을 성공하고 새로운 세션이 생성된 시점)
    @PostMapping("/cart/DbToRedis/{sessionId}/{customerLoginId}")
    public ResponseEntity<Void> saveFromDbToRedis(@PathVariable("sessionId") String sessionId,
        @PathVariable("customerLoginId") String customerLoginId) {

        cartService.transferCartFromDbToRedis(sessionId, customerLoginId);

        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
