package ru.practicum.shareit.item.service;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);

    List<ItemDto> toLItemListDto(List<Item> itemList);

    ItemCommentDto toItemInfoDto(Item item);
}
