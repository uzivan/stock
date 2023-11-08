package com.example.stock.dto;

import com.example.stock.dto_validation.custom_constraints.NotEmptyObject;
import com.example.stock.dto_validation.groups.OnCreate;
import com.example.stock.dto_validation.groups.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotEmptyObject(groups = {OnCreate.class, OnUpdate.class})
public class ProductDto {
    @NotNull(groups = {OnCreate.class})
    private String name;
    @Min(value = 0, groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    private Integer price;
}
