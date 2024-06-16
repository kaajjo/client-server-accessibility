package com.kaajjo.clientserverexam.di.component

import android.app.Application
import com.kaajjo.clientserverexam.di.AppModule
import com.kaajjo.clientserverexam.di.KtorModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import io.ktor.server.netty.NettyApplicationEngine
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        KtorModule::class,
        AppModule::class
    ]
)
interface ServerComponent {

    val server: NettyApplicationEngine

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        @BindsInstance
        fun port(@Named("port") port: Int): Builder

        fun build(): ServerComponent
    }
}