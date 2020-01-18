package com.example.app.bbs2.domain.repository

import com.example.app.bbs2.domain.entity.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Int>