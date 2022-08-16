package at.sunilson.tahomaraffstorecontroller.di

import at.sunilson.tahomaraffstorecontroller.authentication.di.authenticationModule
import org.koin.dsl.module

val mainModule = module {
    includes(authenticationModule)
}