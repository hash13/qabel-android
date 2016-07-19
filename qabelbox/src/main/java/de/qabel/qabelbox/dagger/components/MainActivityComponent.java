package de.qabel.qabelbox.dagger.components;

import dagger.Subcomponent;
import de.qabel.qabelbox.activities.MainActivity;
import de.qabel.qabelbox.contacts.dagger.ContactDetailsComponent;
import de.qabel.qabelbox.contacts.dagger.ContactDetailsModule;
import de.qabel.qabelbox.contacts.dagger.ContactsComponent;
import de.qabel.qabelbox.contacts.dagger.ContactsModule;
import de.qabel.qabelbox.dagger.modules.ChatModule;
import de.qabel.qabelbox.dagger.modules.FileBrowserModule;
import de.qabel.qabelbox.dagger.modules.MainActivityModule;
import de.qabel.qabelbox.dagger.scopes.ActivityScope;

@ActivityScope
@Subcomponent(
        modules = MainActivityModule.class
)
public interface MainActivityComponent {

    void inject(MainActivity activity);
    MainActivity mainActivity();

    ChatComponent plus(ChatModule chatModule);
    FileBrowserComponent plus(FileBrowserModule fileBrowserModule);

    ContactsComponent plus(ContactsModule contactsModule);
    ContactDetailsComponent plus(ContactDetailsModule contactDetailsModule);

}

