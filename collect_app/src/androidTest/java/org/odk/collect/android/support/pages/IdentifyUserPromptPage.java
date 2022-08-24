package org.odk.collect.android.support.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.odk.collect.android.R;

public class IdentifyUserPromptPage extends Page<IdentifyUserPromptPage> {

    private final String formName;

    public IdentifyUserPromptPage(String formName) {
        super();
        this.formName = formName;
    }

    @Override
    public IdentifyUserPromptPage assertOnPage() {
        assertToolbarTitle(formName);
        onView(withText(getTranslatedString(R.string.enter_identity))).check(matches(isDisplayed()));
        return this;
    }

    public IdentifyUserPromptPage enterIdentity(String identity) {
        onView(withHint(getTranslatedString(R.string.identity))).perform(replaceText(identity));
        return this;
    }

    public <D extends Page<D>> D clickKeyboardEnter(D destination) {
        onView(withHint(getTranslatedString(R.string.identity))).perform(pressImeActionButton());
        return destination.assertOnPage();
    }

    public IdentifyUserPromptPage clickKeyboardEnterWithValidationError() {
        onView(withHint(getTranslatedString(R.string.identity))).perform(pressImeActionButton());
        return this.assertOnPage();
    }

    public MainMenuPage pressClose() {
        onView(withContentDescription(getTranslatedString(R.string.close))).perform(click());
        return new MainMenuPage().assertOnPage();
    }
}
