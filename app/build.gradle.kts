plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "org.wastingnotime.contactsmobile"
    compileSdk = 36

    val contactsBffEnvironment = providers.gradleProperty("contactsBffEnvironment")
        .orElse("emulator")
        .get()
        .trim()
        .lowercase()
    val contactsBffEmulatorBaseUrl = providers.gradleProperty("contactsBffEmulatorBaseUrl")
        .orElse("http://10.0.2.2:8010")
        .get()
    val contactsBffLocalDeviceBaseUrl = providers.gradleProperty("contactsBffLocalDeviceBaseUrl")
        .orElse("http://127.0.0.1:8010")
        .get()
    val contactsBffProductionBaseUrl = providers.gradleProperty("contactsBffProductionBaseUrl")
        .orElse("")
        .get()
    val contactsBffAuthSubject = providers.gradleProperty("contactsBffAuthSubject")
        .orElse("admin-user")
        .get()
    val contactsBffAuthRoles = providers.gradleProperty("contactsBffAuthRoles")
        .orElse("admin")
        .get()

    require(contactsBffEnvironment in setOf("emulator", "local_device", "local-device", "device", "production")) {
        "contactsBffEnvironment must be emulator, local_device, or production."
    }
    require(contactsBffEnvironment != "production" || contactsBffProductionBaseUrl.isNotBlank()) {
        "contactsBffProductionBaseUrl must be set when contactsBffEnvironment=production."
    }

    defaultConfig {
        applicationId = "org.wastingnotime.contactsmobile"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "CONTACTS_BFF_ENVIRONMENT",
            "\"$contactsBffEnvironment\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_BFF_EMULATOR_BASE_URL",
            "\"$contactsBffEmulatorBaseUrl\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_BFF_LOCAL_DEVICE_BASE_URL",
            "\"$contactsBffLocalDeviceBaseUrl\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_BFF_PRODUCTION_BASE_URL",
            "\"$contactsBffProductionBaseUrl\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_BFF_AUTH_SUBJECT",
            "\"$contactsBffAuthSubject\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_BFF_AUTH_ROLES",
            "\"$contactsBffAuthRoles\"",
        )
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.2")

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
