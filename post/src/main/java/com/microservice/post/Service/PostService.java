package com.microservice.post.Service;

import com.microservice.post.Configuration.RestTemplateConfig;
import com.microservice.post.Entity.Post;
import com.microservice.post.Payload.PostDto;
import com.microservice.post.Repositry.PostRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class PostService {
    @Autowired
    private PostRepositary postRepo;
    @Autowired
    private RestTemplateConfig restTemplate;
    public Post savePost(Post post){
        String postId= UUID.randomUUID().toString();
        post.setId(postId);
        Post savedpost=postRepo.save(post);
        return savedpost;
    }

    public Post findPostByid(String postId) {
       Post post= postRepo.findById(postId).get();
        return post;
    }

    public PostDto getPostWithComments(String postId) {
        Post post = postRepo.findById(postId).get();
        ArrayList comments = restTemplate.getRestTemplate().getForObject("http://COMMENT-SERVICE/api/comments/" + postId, ArrayList.class);
        PostDto postDto=new PostDto();
        postDto.setPostId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setTittle(post.getTittle());
        postDto.setDescription(post.getDescription());
        postDto.setComments(comments);
        return postDto;
    }
}
