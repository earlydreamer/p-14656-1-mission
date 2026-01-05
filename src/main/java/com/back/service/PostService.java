package com.back.service;

import com.back.document.Post;
import com.back.exception.PostNotFoundException;
import com.back.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public long count() {
        return postRepository.count();
    }

    public Post create(String title, String content, String author) {
        Post post = new Post(title, content, author);
        return postRepository.save(post);
        //⚠️ JPA와의 차이점 - 영속성 컨텍스트 없음
        //JPA: save() 후 영속성 컨텍스트에서 관리되며, 같은 트랜잭션 내에서 변경 감지(Dirty Checking)로 자동 저장
        //Elasticsearch: 영속성 컨텍스트가 없으므로 변경 시마다 save() 호출 필수
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(String id) {
        return postRepository.findById(id)
                .orElseThrow(()->new PostNotFoundException("Post not found with id: " + id)); }


    public Post update(String id, String title, String content) {
        Post post = findById(id);
        if (title != null){
            post.setTitle(title);
        }
        if (content != null){
            post.setContent(content);
        }
        post.setLastModifiedAt(java.time.OffsetDateTime.now());
        return postRepository.save(post);
    }

}