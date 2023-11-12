package com.example.board.service;

import com.example.board.domain.Entity.BoardEntity;
import com.example.board.controller.repository.BoardRepository;
import com.example.board.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MainService {
    private BoardRepository boardRepository;

    private static final int BLOCK_PAGE_NUM_COUNT =5;
    private static final int PAGE_POST_COUNT =4;

    @Transactional
    public List<BoardDto> getBoardlist(Integer pageNum){
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum -1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));

        List<BoardEntity> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(BoardEntity boardEntity : boardEntities){
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }


        return boardDtoList;


    }
    @Transactional
    public Long getBoardCount(){
        return boardRepository.count();
    }
    @Transactional
    public BoardDto getPost(Long id){
        Optional<BoardEntity> boardEntityWrapper =boardRepository.findById(id);
        BoardEntity boardEntity = boardEntityWrapper.get();
        return this.convertEntityToDto(boardEntity);


    }
    @Transactional
    public Long savePost(BoardDto boardDto){return boardRepository.save(boardDto.toEntity()).getId();}

    @Transactional
    public void DeletePost(Long id) {boardRepository.deleteById(id);}

    @Transactional
    public List<BoardDto> searchPosts(String keyword){
        List<BoardEntity> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if(boardEntities.isEmpty())
            return boardDtoList;

        for(BoardEntity boardEntity : boardEntities){
            boardDtoList.add(this.convertEntityToDto(boardEntity));

        }
        return boardDtoList;


    }
    public Integer[] getPageList(Integer curPageNum){
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        Integer totalLastPageNum =(int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;

        // 페이지 번호 할당
        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
    }

    private BoardDto convertEntityToDto(BoardEntity boardEntity){
        return BoardDto.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .createdDate(boardEntity.getCreatedDate())
                .build();

    }
}
