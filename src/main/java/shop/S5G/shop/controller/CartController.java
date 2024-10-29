package shop.S5G.shop.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.S5G.shop.dto.cart.request.CartPutRequestDto;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.service.CartService;

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
    public ResponseEntity<List<CartBooksResponseDto>> lookUpAllBooks(@PathVariable("sessionId") String sessionId) {
        List<CartBooksResponseDto> cartBooks = cartService.lookUpAllBooks(sessionId);

        return new ResponseEntity<>(cartBooks, HttpStatus.OK);
    }
}
