package com.microservice.post.Controler;
import com.microservice.post.Entity.Post;
import com.microservice.post.Payload.PostDto;
import com.microservice.post.Service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> savepost(@RequestBody Post post) {
        Post newpost1 = postService.savePost(post);
        return new ResponseEntity<>(newpost1, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable String postId) {
        Post postByid = postService.findPostByid(postId);
       return new ResponseEntity<>(postByid, HttpStatus.CREATED);
    }
//    @GetMapping("/{postId}")
//    public Post getPostById(@PathVariable String postId) {
//        Post postByid = postService.findPostByid(postId);
//        return postByid;
//    }
@GetMapping("{postId}/comment")
@CircuitBreaker(name="commentBreaker", fallbackMethod = "commentFallback")
public ResponseEntity<PostDto> getPostWithComments(@PathVariable String postId){
      PostDto postDto=postService.getPostWithComments(postId);
        return new ResponseEntity<>(postDto,HttpStatus.OK);
}
public ResponseEntity<PostDto> commentFallback(String postId,Exception ex){//whatever give fallback method all varibale are same
        System.out.println("Fallback is exicuted because service is down :"+ex.getMessage());
        ex.printStackTrace();
        PostDto dto=new PostDto();
        dto.setPostId("1234");
        dto.setContent("service Down");
        dto.setTittle("service Down");
        dto.setDescription("service Down");
        return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
}

}
