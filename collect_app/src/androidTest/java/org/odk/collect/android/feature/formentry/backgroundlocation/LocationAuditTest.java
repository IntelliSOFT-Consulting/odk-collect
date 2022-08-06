package org.odk.collect.android.feature.formentry.backgroundlocation;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.odk.collect.android.R;
import org.odk.collect.android.support.rules.FormActivityTestRule;
import org.odk.collect.android.support.rules.ResetStateRule;
import org.odk.collect.android.support.rules.TestRuleChain;

public class LocationAuditTest {
    private static final String LOCATION_AUDIT_FORM = "location-audit.xml";

    public FormActivityTestRule rule = new FormActivityTestRule(LOCATION_AUDIT_FORM, "Audit with Location");

    @Rule
    public RuleChain copyFormChain = TestRuleChain.chain()
            .around(new ResetStateRule())
            .around(rule);

    @Test
    public void locationCollectionSnackbar_ShouldBeDisplayedAtFormLaunch() {
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(String.format(ApplicationProvider.getApplicationContext().getString(R.string.background_location_enabled), "⋮"))));
    }

    @Test
    public void locationCollectionToggle_ShouldBeAvailable() {
        rule.startInFormEntry()
                .clickOptionsIcon()
                .assertText(R.string.track_location);
    }
}
