package com.mercierlucas.feedarticlescompose.data.network.api

import com.mercierlucas.feedarticlescompose.data.network.dtos.CreaArticleDto
import com.mercierlucas.feedarticlescompose.data.network.dtos.GetAllArticlesDto
import com.mercierlucas.feedarticlescompose.data.network.dtos.GetOneArticleDto
import com.mercierlucas.feedarticlescompose.data.network.dtos.RegisterDto
import com.mercierlucas.feedarticlescompose.data.network.dtos.ReturnLoginDto
import com.mercierlucas.feedarticlescompose.data.network.dtos.UpdateArticleDto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @PUT(ApiRoutes.REGISTER)
    suspend fun registerUserAndGetToken(
        @Body registerDto: RegisterDto
    ) : Response<ReturnLoginDto>?

    @FormUrlEncoded
    @POST(ApiRoutes.LOGIN)
    suspend fun loginUserAndGetToken(
        @Field("login") userLogin : String,
        @Field("mdp") userPassword : String
    ) : Response<ReturnLoginDto>?

    @GET(ApiRoutes.GET_ALL_ARTICLES)
    suspend fun getAllArticles(
        @Header("token") token: String?
    ) : Response<GetAllArticlesDto>?

    @GET(ApiRoutes.GET_ONE_ARTICLE)
    suspend fun getOneArticle(
        @Header("token") token : String?,
        @Path("id") id : Long,
    ) : Response<GetOneArticleDto>?

    @POST(ApiRoutes.UPDATE_ARTICLE)
    suspend fun updateArticleEdited(
        @Path("id") id : Long,
        @Header("token") token : String?,
        @Body updateArticle : UpdateArticleDto
    ) : Response<Unit>?

    @DELETE(ApiRoutes.DELETE_ARTICLE)
    suspend fun deleteArticleEdited(
        @Path("id") id : Long,
        @Header("token") token : String?
    ) : Response<Unit>?

    @PUT(ApiRoutes.CREATE_ARTICLE)
    suspend fun createNewArticle(
        @Header("token") token : String?,
        @Body creaArticle: CreaArticleDto
    ) : Response<Unit>?


}