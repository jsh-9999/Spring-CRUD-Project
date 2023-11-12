package com.example.board.controller;


import com.example.board.dto.BoardDto;
import com.example.board.service.MainService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController{
    private MainService mainService;

    @GetMapping("/")
    public String list(Model model, @RequestParam(value ="page", defaultValue = "1") Integer pageNum){
        List<BoardDto> boardList = mainService.getBoardlist(pageNum);
        Integer[] pageList= mainService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/list";
    }

    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = mainService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/detail.html";
    }
    @GetMapping("/post")
    public String write(){
        return "board/write.html";
    }


    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        mainService.savePost(boardDto);

        return "redirect:/";
    }

    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = mainService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/update.html";
    }

    @PostMapping("/post/edit/{no}")
    public String update(BoardDto boardDTO) {
        mainService.savePost(boardDTO);

        return "redirect:/";
    }

    @PostMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        mainService.DeletePost(no);

        return "redirect:/";
    }
    @GetMapping("/board/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = mainService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/list.html";
    }
}