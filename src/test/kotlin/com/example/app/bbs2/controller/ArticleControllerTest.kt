package com.example.app.bbs2.controller

import com.example.app.bbs2.domain.entity.Article
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class ArticleControllerTests {
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var target: ArticleController

    @Before
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(target).build()
    }

    @Test
    fun registerArticleTest() {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/")
                        .param("name", "test")
                        .param("title", "test")
                        .param("contents", "test")
                        .param("articleKey", "test")
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
    }

    @Test
    fun getArticleListTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("articles"))
                .andExpect(view().name("index"))
    }

    @Test
    fun getArticleEditNotExistsIdTest(){
        mockMvc.perform(
                MockMvcRequestBuilders.get("/edit/" + 0)
        )
                .andExpect((status().is3xxRedirection))
                .andExpect(view().name("redirect:/"))
    }

    @Test
    @Sql(statements = ["INSERT INTO article (name, title, contents, article_key) VALUES ('test', 'test', 'test', 'test');"])
    fun getArticleEditExistsIdTest(){
        val latestArticle: Article = target.articleRepository.findAll().last()

        mockMvc.perform(
                MockMvcRequestBuilders.get("/edit/" + latestArticle.id)
        )
                .andExpect(status().isOk)
                .andExpect(view().name("edit"))
    }

    @Test
    fun updateArticleNotExistsArticleTest() {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/update")
                        .param("id", "0")
                        .param("name", "test")
                        .param("title", "test")
                        .param("contents", "test")
                        .param("articleKey", "err.")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
    }

    @Test
    @Sql(statements = ["INSERT INTO article (name, title, contents, article_key,register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun updateArticleNotMatchArticleKeyTest() {
        val latestArticle: Article = target.articleRepository.findAll().last()

        mockMvc.perform(
                MockMvcRequestBuilders.post("/update")
                        .param("id", latestArticle.id.toString())
                        .param("name", latestArticle.name)
                        .param("title", latestArticle.title)
                        .param("contents", latestArticle.contents)
                        .param("articleKey", "err.")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/edit/${latestArticle.id.toString()}"))
    }

    @Test
    @Sql(statements = ["INSERT INTO article (name, title, contents, article_key,register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun updateArticleExistsArticleTest() {
        val latestArticle: Article = target.articleRepository.findAll().last()

        mockMvc.perform(
                MockMvcRequestBuilders.post("/update")
                        .param("id", latestArticle.id.toString())
                        .param("name", latestArticle.name)
                        .param("title", latestArticle.title)
                        .param("contents", latestArticle.contents)
                        .param("articleKey", latestArticle.articleKey)
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
    }
}