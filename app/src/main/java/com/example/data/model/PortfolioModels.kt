package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class PortfolioCategory(val label: String) {
    ALL("All Works"),
    BRANDING("Logos & Branding"),
    WEB_UI("Web & UI/UX"),
    SOCIAL("Social & Posters")
}

enum class MockupFrameType(val badge: String) {
    LOGO_STUDIO("2D Logo Presentation"),
    BRAND_BOARD("2D Brand Identity Board"),
    WEBSITE_BROWSER("2D Web Browser Frame"),
    MOBILE_UI("2D Mobile UI Showcase"),
    POSTER_FRAME("2D Poster & Ad Frame")
}

data class PortfolioProject(
    val id: String,
    val title: String,
    val category: PortfolioCategory,
    val categoryLabel: String,
    val mockupType: MockupFrameType,
    @DrawableRes val drawableRes: Int,
    val summary: String,
    val clientOrBrand: String
)

@IgnoreExtraProperties
data class JobApplication(
    @DocumentId val id: String = "",
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val cnicMasked: String = "",
    val cnicDigest: String = "",
    val position: String = "",
    val course: String = "",
    val customCourse: String? = null,
    val dedupeKey: String = "",
    val status: String = "submitted",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

object PortfolioCatalog {
    val jobPositions = listOf("Teacher", "Manager")

    val jobCourses = listOf(
        "Graphic Designing",
        "Artificial Intelligence (AI)",
        "Web Designing",
        "UI/UX Designing",
        "Microsoft Office",
        "Database",
        "Other"
    )

    val projects: List<PortfolioProject> = listOf(
        PortfolioProject(
            id = "logo_katie_lawson",
            title = "Katie Lawson — Monogram & Botanical Mark",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Logo Design",
            mockupType = MockupFrameType.LOGO_STUDIO,
            drawableRes = R.drawable.work_logo_katie_lawson,
            summary = "Editorial serif monogram paired with delicate botanical line art for a fashion designer identity.",
            clientOrBrand = "Katie Lawson"
        ),
        PortfolioProject(
            id = "logo_daniel_gallego",
            title = "Daniel Gallego Company — Corporate Crest",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Logo Design",
            mockupType = MockupFrameType.LOGO_STUDIO,
            drawableRes = R.drawable.work_logo_daniel_gallego,
            summary = "Architectural ribbon emblem with cyan gradient pillars and laurel accents on a dark studio backdrop.",
            clientOrBrand = "Daniel Gallego Company"
        ),
        PortfolioProject(
            id = "logo_studio_shodwe",
            title = "Studio Shodwe — Geometric Monogram",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Brand Logo",
            mockupType = MockupFrameType.LOGO_STUDIO,
            drawableRes = R.drawable.work_logo_studio_shodwe,
            summary = "High-contrast geometric lettermark built with bold architectural strokes on pure black.",
            clientOrBrand = "Studio Shodwe"
        ),
        PortfolioProject(
            id = "logo_larana",
            title = "Larana Fashion Store — Boutique Emblem",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Logo Design",
            mockupType = MockupFrameType.LOGO_STUDIO,
            drawableRes = R.drawable.work_logo_larana,
            summary = "Expressive magenta silhouette and signature script wordmark for a retail fashion brand.",
            clientOrBrand = "Larana Fashion Store"
        ),
        PortfolioProject(
            id = "logo_lars_peeters",
            title = "Lars Peeters Studio — Signature Crest",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Logo Design",
            mockupType = MockupFrameType.LOGO_STUDIO,
            drawableRes = R.drawable.work_logo_lars_peeters,
            summary = "Warm gold brush wreath framing a hand-lettered monogram on deep espresso.",
            clientOrBrand = "Lars Peeters Studio"
        ),
        PortfolioProject(
            id = "logo_neil_tran",
            title = "Neil Tran Company — Architectural Mark",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Logo Design",
            mockupType = MockupFrameType.LOGO_STUDIO,
            drawableRes = R.drawable.work_logo_neil_tran,
            summary = "Minimalist interlocked monogram with structured sans-serif lockup on charcoal.",
            clientOrBrand = "Neil Tran Company"
        ),
        PortfolioProject(
            id = "brand_birea",
            title = "BIREA — Complete Visual Brand Identity",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Branding Design",
            mockupType = MockupFrameType.BRAND_BOARD,
            drawableRes = R.drawable.work_brand_birea,
            summary = "Full brand system featuring forest green palette, Montserrat typography, packaging boxes, stationery, and merchandise.",
            clientOrBrand = "BIREA"
        ),
        PortfolioProject(
            id = "brand_reltor",
            title = "Reltor — Audio Lifestyle Brand System",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Branding Design",
            mockupType = MockupFrameType.BRAND_BOARD,
            drawableRes = R.drawable.work_brand_reltor,
            summary = "Letter R + earphone symbol identity with custom monogram pattern, packaging, apparel, and corporate stationery.",
            clientOrBrand = "Reltor"
        ),
        PortfolioProject(
            id = "brand_pivot",
            title = "PIVOT — Apparel & Lifestyle Brand Suite",
            category = PortfolioCategory.BRANDING,
            categoryLabel = "Branding Design",
            mockupType = MockupFrameType.BRAND_BOARD,
            drawableRes = R.drawable.work_brand_pivot,
            summary = "Modern burgundy and cream brand identity across caps, woven labels, shopping bags, business cards, and bottles.",
            clientOrBrand = "PIVOT"
        ),
        PortfolioProject(
            id = "web_veora",
            title = "Véora — Modern Fashion E-Commerce UI",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "Website Design",
            mockupType = MockupFrameType.WEBSITE_BROWSER,
            drawableRes = R.drawable.work_web_veora,
            summary = "Responsive fashion storefront featuring lavender gradients, mobile companion preview, category grid, and best sellers.",
            clientOrBrand = "Véora"
        ),
        PortfolioProject(
            id = "web_timeless",
            title = "Timeless By Nature — Luxury Menswear Web",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "Website Design",
            mockupType = MockupFrameType.WEBSITE_BROWSER,
            drawableRes = R.drawable.work_web_timeless,
            summary = "Editorial dark-and-sand menswear storefront with high-contrast serif typography, product swatches, and journal cards.",
            clientOrBrand = "Timeless By Nature"
        ),
        PortfolioProject(
            id = "web_gizmohub",
            title = "GizmoHub — Smart Tech E-Commerce Store",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "Website Design",
            mockupType = MockupFrameType.WEBSITE_BROWSER,
            drawableRes = R.drawable.work_web_gizmohub,
            summary = "Clean electronics retail layout with hero showcase, category cards, top picks, and promotional banners.",
            clientOrBrand = "GizmoHub"
        ),
        PortfolioProject(
            id = "ui_salad_app",
            title = "Fresh Salad Ordering — Dual Mobile UI",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "UI/UX Design",
            mockupType = MockupFrameType.MOBILE_UI,
            drawableRes = R.drawable.work_ui_salad_app,
            summary = "Clean healthy-food mobile app interface showcasing special offers, ingredient chips, and nutrition breakdown.",
            clientOrBrand = "Salad Concept App"
        ),
        PortfolioProject(
            id = "ui_leafora_app",
            title = "Leafora — Botanical Plant Shop Mobile UI",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "UI/UX Design",
            mockupType = MockupFrameType.MOBILE_UI,
            drawableRes = R.drawable.work_ui_leafora_app,
            summary = "Deep forest-green four-screen mobile UX flow covering onboarding, catalog browsing, plant care specs, and cart checkout.",
            clientOrBrand = "Leafora"
        ),
        PortfolioProject(
            id = "ui_burger_app",
            title = "Good Food Good Mood — Burger Delivery UI",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "UI/UX Design",
            mockupType = MockupFrameType.MOBILE_UI,
            drawableRes = R.drawable.work_ui_burger_app,
            summary = "Warm amber dark-mode food delivery UI with category tabs, combo banners, and interactive meal customization.",
            clientOrBrand = "Burger Concept App"
        ),
        PortfolioProject(
            id = "ui_dark_dining",
            title = "Culinary Dark Theme — Multi-Screen App UI",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "UI/UX Design",
            mockupType = MockupFrameType.MOBILE_UI,
            drawableRes = R.drawable.work_ui_dark_dining,
            summary = "Nine-screen luxury dark dining mobile experience with gold call-to-actions and dish photography.",
            clientOrBrand = "Culinary App"
        ),
        PortfolioProject(
            id = "ui_tortilla_app",
            title = "TORTILLA — California Burritos Mobile UI",
            category = PortfolioCategory.WEB_UI,
            categoryLabel = "UI/UX Design",
            mockupType = MockupFrameType.MOBILE_UI,
            drawableRes = R.drawable.work_ui_tortilla_app,
            summary = "Warm terracotta mobile ordering experience featuring location selector, loyalty rewards, and popular picks.",
            clientOrBrand = "Tortilla"
        ),
        PortfolioProject(
            id = "poster_fashion_style",
            title = "New Fashion Style — Saharish Collection",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Poster Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_poster_fashion_style,
            summary = "Editorial seasonal discount promotional graphic with warm tan geometric framing.",
            clientOrBrand = "Saharish Collection"
        ),
        PortfolioProject(
            id = "poster_nike_shoes",
            title = "Pick The Perfect Pair — Footwear Promo",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Social Media Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_poster_nike_shoes,
            summary = "High-energy neon green footwear social media campaign banner with bold italic typography.",
            clientOrBrand = "Al Awais Graphic"
        ),
        PortfolioProject(
            id = "poster_food_menu",
            title = "Today's Special Food Menu — Print & Digital",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Poster Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_poster_food_menu,
            summary = "Charcoal slate restaurant menu layout featuring vibrant orange brush accents, main dishes, and beverages.",
            clientOrBrand = "Flavyure Food"
        ),
        PortfolioProject(
            id = "poster_music_earbuds",
            title = "Wireless Audio — Music Campaign Post",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Social Media Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_poster_music_earbuds,
            summary = "Teal product showcase blending music player UI elements with floating wireless earbuds.",
            clientOrBrand = "Audio Promo"
        ),
        PortfolioProject(
            id = "poster_ai_for_hrs",
            title = "AI FOR HRS — Rimberio Tech Flyer",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Poster Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_poster_ai_for_hrs,
            summary = "Clean futuristic tech workshop poster highlighting AI tools with robotic hand visual composition.",
            clientOrBrand = "Rimberio"
        ),
        PortfolioProject(
            id = "social_tokyo_gaming",
            title = "Tokyo Store — Gaming & Tech Social Grid",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Social Media Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_social_tokyo_gaming,
            summary = "Four-tile social media campaign suite for PS5 Slim, AirPods Pro 3, and gaming accessories.",
            clientOrBrand = "Tokyo Store"
        ),
        PortfolioProject(
            id = "social_apple_blue",
            title = "Tech Retail Blue Series — Social Ads",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Social Media Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_social_apple_blue,
            summary = "Coordinated cobalt-blue promotional templates for smartwatches, smartphones, and audio devices.",
            clientOrBrand = "Apple Tech Series"
        ),
        PortfolioProject(
            id = "social_purple_tech",
            title = "Purple Gadget Series — Social Media Kit",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Social Media Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_social_purple_tech,
            summary = "Vibrant purple and white 4-post social media set for headphones, chargers, earbuds, and smartwatches.",
            clientOrBrand = "Tech Accessories"
        ),
        PortfolioProject(
            id = "social_apple_showcase",
            title = "Authorized Tech Campaign — 9-Post Grid",
            category = PortfolioCategory.SOCIAL,
            categoryLabel = "Social Media Design",
            mockupType = MockupFrameType.POSTER_FRAME,
            drawableRes = R.drawable.work_social_apple_showcase,
            summary = "High-impact nine-poster campaign showcasing lifestyle tech photography, sleek typography, and feature icons.",
            clientOrBrand = "Tech Retail Campaign"
        )
    )
}
