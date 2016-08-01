package de.qabel.qabelbox.chat.view.presenters

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.stub
import de.qabel.core.repository.entities.ChatDropMessage
import de.qabel.qabelbox.BuildConfig
import de.qabel.qabelbox.SimpleApplication
import de.qabel.qabelbox.chat.dto.ChatMessage
import de.qabel.qabelbox.chat.dto.MessagePayloadDto
import de.qabel.qabelbox.chat.interactor.MockChatUseCase
import de.qabel.qabelbox.chat.view.views.ChatView
import de.qabel.qabelbox.util.IdentityHelper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricGradleTestRunner::class)
@Config(application = SimpleApplication::class, constants = BuildConfig::class)
class MainChatPresenterTest {

    val identity = IdentityHelper.createIdentity("identity", null)
    val contact = IdentityHelper.createContact("contact_name")
    val view = mock(ChatView::class.java)
    val now = Date()
    val textPayload = MessagePayloadDto.TextMessage("Text")
    val sampleMessage = ChatMessage(identity, contact, ChatDropMessage.Direction.INCOMING, now, textPayload)
    val useCase = spy(MockChatUseCase(sampleMessage, contact, listOf()))

    @Test fun testEmptyStartup() {
        MainChatPresenter(view, useCase)
        verify(view).showEmpty()
    }

    @Test fun testStartupWithMessages() {
        useCase.messages = listOf(sampleMessage,
                sampleMessage.copy(messagePayload = MessagePayloadDto.TextMessage("test2")))
        MainChatPresenter(view, useCase)
        verify(view).showMessages(useCase.messages)
    }

    @Test fun messageIsSent() {
        stub(view.messageText).toReturn("Text")
        val presenter = MainChatPresenter(view, useCase)
        presenter.sendMessage()
        verify(useCase).send(view.messageText)
        verify(view).showMessages(listOf(sampleMessage))
        verify(view).messageText = ""
    }

    @Test fun emptyMessageIsIgnored() {
        stub(view.messageText).toReturn("")
        val presenter = MainChatPresenter(view, useCase)
        presenter.sendMessage()
        verify(useCase, never()).send(any())
        verify(view, never()).showMessages(any())
    }
}
