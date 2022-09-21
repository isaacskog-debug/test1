package com.example.securitystart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentsController {

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("comments")
    public String comments(Model model) {
        List<Comment> comments = (List<Comment>)commentRepository.findAll();
        model.addAttribute("comments", comments);
        return "comments";
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam String text){
        Comment comment = new Comment(text);
        commentRepository.save(comment);
        return "redirect:comments";
    }
}
