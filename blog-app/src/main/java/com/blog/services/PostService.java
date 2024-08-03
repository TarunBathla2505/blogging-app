package com.blog.services;

import com.blog.entities.Post;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {
    //create
    public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
    //update
    public PostDto updatePost(PostDto postDto,Integer postId);
    //delete
    public void deletePost(Integer postId);
    //get
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    public PostDto getPostById(Integer postId);
    public List<PostDto> getPostsByCategory(Integer CategoryId);
    public List<PostDto> getPostsByUser(Integer userId);
    public List<PostDto> searchPosts(String keyword);
}
