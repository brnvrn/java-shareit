package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.CommentMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentMapperImplTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void testToCommentDto_Success() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Отлично");
        comment.setCreated(LocalDateTime.now());

        CommentDto commentDto = commentMapper.toCommentDto(comment);

        assertNotNull(commentDto);
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getCreated(), commentDto.getCreated());
    }

    @Test
    void testToCommentDto_NullInput() {
        CommentDto commentDto = commentMapper.toCommentDto(null);

        assertNull(commentDto);
    }

    @Test
    void testToComment_Success() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Отлично");
        commentDto.setCreated(LocalDateTime.now());

        Comment comment = commentMapper.toComment(commentDto);

        assertNotNull(comment);
        assertEquals(commentDto.getId(), comment.getId());
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(commentDto.getCreated(), comment.getCreated());
    }

    @Test
    void testToComment_NullInput() {
        Comment comment = commentMapper.toComment(null);

        assertNull(comment);
    }

    @Test
    void testToListDto_Success() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("Отлично");
        comment1.setCreated(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Ок");
        comment2.setCreated(LocalDateTime.now());

        List<Comment> comments = Arrays.asList(comment1, comment2);

        List<CommentDto> commentDtoList = commentMapper.toListDto(comments);

        assertNotNull(commentDtoList);
        assertEquals(2, commentDtoList.size());
        assertEquals(comment1.getId(), commentDtoList.get(0).getId());
        assertEquals(comment2.getId(), commentDtoList.get(1).getId());
    }

    @Test
    void testToListDto_NullInput() {
        List<CommentDto> commentDtoList = commentMapper.toListDto(null);

        assertNull(commentDtoList);
    }

    @Test
    void testToListDto_EmptyList() {
        List<CommentDto> commentDtoList = commentMapper.toListDto(Arrays.asList());

        assertNotNull(commentDtoList);
        assertTrue(commentDtoList.isEmpty());
    }
}