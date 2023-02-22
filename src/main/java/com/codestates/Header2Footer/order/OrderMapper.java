package com.codestates.Header2Footer.order;

import com.codestates.Header2Footer.coffee.Coffee;
import com.codestates.Header2Footer.member.Member;
import com.codestates.Header2Footer.order_coffee.OrderCoffee;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order orderPatchDtoToOrder(OrderDto.Patch orderPatchDto);
    List<OrderDto.OrderCoffeeResponse> ordersToOrderResponseDtos(List<Order> orders);

    default Order orderPostDtoToOrder(OrderDto.Post orderPostDto) {
        Order order = new Order();
        Member member = new Member();
        member.setMemberId(orderPostDto.getMemberId());

        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                        .map(orderCoffeeDto -> {
                            OrderCoffee orderCoffee = new OrderCoffee();
                            Coffee coffee = new Coffee();
                            coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                            orderCoffee.addOrder(order);
                            orderCoffee.addCoffee(coffee);
                            orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                            return orderCoffee;
                        }).collect(Collectors.toList());
        order.setMember(member);
        order.setOrderCoffees(orderCoffees);

        return order;
    }

    default OrderDto.Response orderToOrderResponseDto(Order order){
        List<OrderCoffee> orderCoffees = order.getOrderCoffees();

        OrderDto.Response orderResponseDto = new OrderDto.Response();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setMember(order.getMember());
        orderResponseDto.setOrderStatus(order.getOrderStatus());
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        orderResponseDto.setOrderCoffees(
                orderCoffeesToOrderCoffeeResponseDtos(orderCoffees)
        );

        return orderResponseDto;
    }

    default List<OrderDto.OrderCoffeeResponse> orderCoffeesToOrderCoffeeResponseDtos(
                                                    List<OrderCoffee> orderCoffees) {
        return orderCoffees
                .stream()
                .map(orderCoffee -> OrderDto.OrderCoffeeResponse
                        .builder()
                        .coffeeId(orderCoffee.getCoffee().getCoffeeId())
                        .quantity(orderCoffee.getQuantity())
                        .price(orderCoffee.getCoffee().getPrice())
                        .korName(orderCoffee.getCoffee().getKorName())
                        .engName(orderCoffee.getCoffee().getEngName())
                        .build())
                .collect(Collectors.toList());
    }
}
