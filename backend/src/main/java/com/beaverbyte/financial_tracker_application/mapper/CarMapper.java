package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.*;

import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.payload.request.SignupRequest;

import java.util.Set;

// @Mapper(componentModel = "spring")
// @Mapper
// public interface CarMapper {

//     @Mappings({
//         @Mapping(source = "model", target = "name"),
//         @Mapping(source = "year", target = "productionYear")
//     })

//     CarDto carToCarDto(Car car);

//     @Named("colorToString")
//     default String colorToString(Color color) {
//         return color.name();
//     }

//     @Named("stringToColor")
//     default Color stringToColor(String color) {
//         return Color.valueOf(color);
//     }
// }