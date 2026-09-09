// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    /*
    the first question comes to mine wtf we declare these here?
    the ans is it's primarily for the multimodule

    when we say `apply false` we are saying apply to the modules that use this
    not to the project itself so we can seperate where to use and where not to

    it was mostly useful before version catalog
     */
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.ksp) apply false
}