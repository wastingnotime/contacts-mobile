plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.wastingnotime.contactsmobile"
    compileSdk = 34

    val contactsApiEnvironment = providers.gradleProperty("contactsApiEnvironment")
        .orElse("emulator")
        .get()
        .trim()
        .lowercase()
    val contactsApiEmulatorBaseUrl = providers.gradleProperty("contactsApiEmulatorBaseUrl")
        .orElse("http://10.0.2.2:8010")
        .get()
    val contactsApiLocalDeviceBaseUrl = providers.gradleProperty("contactsApiLocalDeviceBaseUrl")
        .orElse("http://127.0.0.1:8010")
        .get()
    val contactsApiProductionBaseUrl = providers.gradleProperty("contactsApiProductionBaseUrl")
        .orElse("")
        .get()

    require(contactsApiEnvironment in setOf("emulator", "local_device", "local-device", "device", "production")) {
        "contactsApiEnvironment must be emulator, local_device, or production."
    }
    require(contactsApiEnvironment != "production" || contactsApiProductionBaseUrl.isNotBlank()) {
        "contactsApiProductionBaseUrl must be set when contactsApiEnvironment=production."
    }

    defaultConfig {
        applicationId = "com.wastingnotime.contactsmobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "CONTACTS_API_ENVIRONMENT",
            "\"$contactsApiEnvironment\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_API_EMULATOR_BASE_URL",
            "\"$contactsApiEmulatorBaseUrl\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_API_LOCAL_DEVICE_BASE_URL",
            "\"$contactsApiLocalDeviceBaseUrl\"",
        )
        buildConfigField(
            "String",
            "CONTACTS_API_PRODUCTION_BASE_URL",
            "\"$contactsApiProductionBaseUrl\"",
        )
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
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

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
