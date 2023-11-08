package com.example.stock.dto;

import com.example.stock.dto_validation.custom_constraints.NotEmptyObject;
import com.example.stock.dto_validation.groups.OnCreate;
import com.example.stock.dto_validation.groups.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotEmptyObject(groups = {OnCreate.class, OnUpdate.class})
public class OrderDto {
    @NotNull(groups = {OnCreate.class})
    private LocalDateTime orderedTime;
    @NotNull(groups = {OnCreate.class})
    private String status;
    @NotNull(groups = {OnCreate.class})
    @Size(min = 1, groups = {OnCreate.class, OnUpdate.class})
    private List<@NotNull @Min(value = 1, groups = {OnCreate.class, OnUpdate.class}) Integer> productId;
}
