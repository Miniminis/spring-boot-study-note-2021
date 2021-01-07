package com.example.study.service;

import com.example.study.interfaces.CRUDInterface;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.OrderGroupApiRequest;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.repository.OrderGroupRepository;
import com.example.study.repository.UserRepository;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderGroupApiService implements CRUDInterface<OrderGroupApiRequest, OrderGroupApiResponse> {

    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Header<OrderGroupApiResponse> create(Header<OrderGroupApiRequest> req) {
        OrderGroupApiRequest apiRequest = req.getData();

        OrderGroup orderGroup = OrderGroup.builder()
                .status(apiRequest.getStatus())
                .orderType(apiRequest.getOrderType())
                .revAddress(apiRequest.getRevAddress())
                .revName(apiRequest.getRevName())
                .paymentType(apiRequest.getPaymentType())
                .totalPrice(apiRequest.getTotalPrice())
                .totalQuantity(apiRequest.getTotalQuantity())
                .orderAt(apiRequest.getOrderAt())
                .arrivalDate(apiRequest.getArrivalDate())
                .user(userRepository.getOne(apiRequest.getUserId()))
                .build();

        OrderGroup newOrderGroup = orderGroupRepository.save(orderGroup);
        return response(newOrderGroup);
    }

    @Override
    public Header<OrderGroupApiResponse> read(Long id) {
        Optional<OrderGroup> optionalOrderGroup = orderGroupRepository.findById(id);
        return optionalOrderGroup.map(this::response)
                .orElseGet(() -> Header.ERROR("일치하는 데이터가 없습니다."));
    }

    @Override
    public Header<OrderGroupApiResponse> update(Header<OrderGroupApiRequest> req) {

        OrderGroupApiRequest apiRequest = req.getData();

        Optional<OrderGroup> orderGroupOptional = orderGroupRepository.findById(apiRequest.getId());
        return orderGroupOptional
                .map(foundOrderGroup -> {
                    foundOrderGroup
                            .setStatus(apiRequest.getStatus())
                            .setOrderType(apiRequest.getOrderType())
                            .setRevAddress(apiRequest.getRevAddress())
                            .setRevName(apiRequest.getRevName())
                            .setPaymentType(apiRequest.getPaymentType())
                            .setTotalPrice(apiRequest.getTotalPrice())
                            .setTotalQuantity(apiRequest.getTotalQuantity())
                            .setOrderAt(apiRequest.getOrderAt())
                            .setArrivalDate(apiRequest.getArrivalDate())
                            .setUser(userRepository.getOne(apiRequest.getUserId()));
                    return foundOrderGroup;
                })
                .map(updatedOrderGroup -> orderGroupRepository.save(updatedOrderGroup))
                .map(this::response)
                .orElseGet(() -> Header.ERROR("일치하는 데이터가 없습니다!"));
    }

    @Override
    public Header delete(Long id) {
        Optional<OrderGroup> orderGroupOptional = orderGroupRepository.findById(id);
        return orderGroupOptional
                .map(foundOrderGroup -> {
                    orderGroupRepository.delete(foundOrderGroup);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("일치하는 데이터가 없습니다!"));
    }

    private Header<OrderGroupApiResponse> response(OrderGroup newOrderGroup) {
        OrderGroupApiResponse response = OrderGroupApiResponse.builder()
                .id(newOrderGroup.getId())
                .status(newOrderGroup.getStatus())
                .orderType(newOrderGroup.getOrderType().getTitle())
                .revAddress(newOrderGroup.getRevAddress())
                .revName(newOrderGroup.getRevName())
                .paymentType(newOrderGroup.getPaymentType())
                .totalPrice(newOrderGroup.getTotalPrice())
                .totalQuantity(newOrderGroup.getTotalQuantity())
                .orderAt(newOrderGroup.getOrderAt())
                .arrivalDate(newOrderGroup.getArrivalDate())
                .userId(newOrderGroup.getUser().getId())
                .build();

        return Header.OK(response);
    }

}
