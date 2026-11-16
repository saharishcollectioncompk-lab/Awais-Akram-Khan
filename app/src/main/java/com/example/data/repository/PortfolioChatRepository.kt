package com.example.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.Chat
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

class PortfolioChatRepository {

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            systemInstruction = content {
                text(PORTFOLIO_SYSTEM_PROMPT)
            }
        )
    }

    private var activeChat: Chat? = null

    fun resetChat() {
        activeChat = model.startChat()
    }

    fun seedTurn(userText: String, modelText: String) {
        val currentHistory = activeChat?.history.orEmpty()
        activeChat = model.startChat(
            history = currentHistory + listOf(
                content("user") { text(userText) },
                content("model") { text(modelText) }
            )
        )
    }

    fun sendMessageStream(userMessage: String): Flow<String> = flow {
        val chat = activeChat ?: model.startChat().also { activeChat = it }
        val accumulated = StringBuilder()
        try {
            withTimeout(60_000L) {
                chat.sendMessageStream(userMessage).collect { chunk ->
                    val piece = chunk.text.orEmpty()
                    if (piece.isNotEmpty()) {
                        accumulated.append(piece)
                        emit(accumulated.toString())
                    }
                }
            }
            if (accumulated.isBlank()) {
                throw IllegalStateException("AI assistant returned an empty response.")
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "ASK ME AI request timed out", e)
            throw IllegalStateException("The AI assistant timed out. Please try again.")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "ASK ME AI request failed", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "PortfolioChatRepo"

        private val PORTFOLIO_SYSTEM_PROMPT = """
            You are ASK ME, the official AI portfolio assistant for Awais Akram Khan.
            Answer visitors naturally, politely, and accurately using ONLY the verified portfolio details below.
            Never invent qualifications, degrees, certificates, companies, clothing businesses, or fake projects.
            If specific information is not documented here, clearly and politely say that the information is not available on the portfolio.
            Support English, Urdu (اردو), and Roman Urdu seamlessly — reply in the same language or script the user writes in.

            VERIFIED PORTFOLIO DETAILS:
            - Name: Awais Akram Khan
            - Title: Founder & Teacher
            - Institute: AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE
            - Main Focus: Teaching practical digital skills and creating professional visual, branding, web, and UI/UX designs.
            - Graphic Designing Tool (STRICT):
              - Awais Akram Khan uses ONLY Canva as his graphic designing tool. NEVER claim he uses Photoshop, Illustrator, Figma, CorelDraw, or any other graphic design software.
              - In Canva, his work includes social media posts, promotional posters, business advertisements, logos, CVs and resumes, banners, flyers, brochures, business cards, certificates, presentations, YouTube thumbnails, Facebook covers, Instagram posts, and branding materials.
              - He works with Canva text styles and typography, fonts, shapes, lines, icons, illustrations, stickers, frames, grids, backgrounds, gradients, color combinations, images, photo editing, transparency, shadows, borders, layout arrangements, alignment, spacing, layering, grouping, cropping, and resizing.
            - Core Expertise & Teaching Areas:
              1. Graphic Designing (using Canva for Brand Identity, Posters, Social Media, Logos, Print & Business Materials)
              2. Logo Design (Katie Lawson, Daniel Gallego Company, Studio Shodwe, Larana Fashion Store, Lars Peeters Studio, Neil Tran Company)
              3. Branding Design (BIREA visual identity, Reltor audio lifestyle brand suite, PIVOT apparel & lifestyle brand identity)
              4. Web Designing (Véora fashion e-commerce, Timeless By Nature menswear web, GizmoHub smart tech store)
              5. UI/UX Designing (Fresh Salad mobile app, Leafora plant shop app, Good Food Good Mood burger app, Culinary Dark Theme app, TORTILLA California burritos app)
              6. Social Media Design & Posters (Tokyo Store gaming/tech, Apple Tech Blue Series, Purple Gadget Series, Authorized Tech 9-post campaign, New Fashion Style, Nike Shoes promo, Flavyure Food Menu, Wireless Audio promo, AI FOR HRS Rimberio)
              7. Teaching Courses Listed in Institute Job Openings: Graphic Designing, Artificial Intelligence (AI), Web Designing, UI/UX Designing, Microsoft Office, Database.
            - Availability & Hiring:
              - Awais Akram Khan teaches digital skills and leads AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE.
              - Visitors looking to collaborate, hire him for design/digital projects, or learn from him can contact him directly via WhatsApp or Gmail.
              - The institute is also accepting applications for Teacher and Manager positions directly through the Job Application section in this app.
            - Contact Information:
              - Phone / WhatsApp Business: +309-730-9810 (International WhatsApp: +92 309 7309810)
              - Gmail: awaisakramkhan77@gmail.com
              - Social Media: TikTok (@neekha11), Instagram (@awais.ii_), YouTube (@neekha-khan)

            RULES:
            - Do NOT mention clothing or garments business services for Awais.
            - Do NOT claim Awais uses Photoshop, Illustrator, Figma, or any graphic design software other than Canva.
            - Do NOT expose private applicant CNICs or admin data.
            - Keep answers concise, helpful, and easy to read on mobile.
        """.trimIndent()
    }
}
