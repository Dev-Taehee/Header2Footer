package com.codestates.Header2Footer.order;

import com.codestates.Header2Footer.member.Member;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        @Positive
        private long memberId;

        @Valid
        private List<OrderDto.OrderCoffee> orderCoffees;

        public Member getMember() {
            Member member = new Member();
            member.setMemberId(memberId);
            return member;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Patch{
        private long orderId;
        private Order.OrderStatus orderStatus;

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }
    }

    @Getter
    @Setter
    public static class Response{
        private long orderId;

        @Setter(AccessLevel.NONE)
        private long memberId;
        private Order.OrderStatus orderStatus;
        private List<OrderDto.OrderCoffeeResponse> orderCoffees;
        private LocalDateTime createdAt;

        public void setMember(Member member) {
            this.memberId = member.getMemberId();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class OrderCoffee{
        @Positive
        private long coffeeId;

        @Positive
        private int quantity;
    }

    @Builder
    @Getter
    public static class OrderCoffeeResponse{
        @Positive
        private long coffeeId;
        @Positive
        private Integer quantity;
        private String korName;
        private String engName;
        private Integer price;
    }
}
