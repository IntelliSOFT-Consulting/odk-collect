/*
 * Copyright (C) 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.geo.geopoly;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.odk.collect.androidtest.ActivityScenarioLauncherRule;
import org.odk.collect.geo.Constants;
import org.odk.collect.geo.DaggerGeoDependencyComponent;
import org.odk.collect.geo.GeoDependencyModule;
import org.odk.collect.geo.R;
import org.odk.collect.geo.ReferenceLayerSettingsNavigator;
import org.odk.collect.geo.support.FakeMapFragment;
import org.odk.collect.geo.support.RobolectricApplication;
import org.odk.collect.location.tracker.LocationTracker;
import org.odk.collect.maps.MapFragmentFactory;
import org.robolectric.shadows.ShadowApplication;

@RunWith(AndroidJUnit4.class)
public class GeoPolyActivityTest {

    private final FakeMapFragment mapFragment = new FakeMapFragment();
    private final LocationTracker locationTracker = mock(LocationTracker.class);

    @Rule
    public ActivityScenarioLauncherRule launcherRule = new ActivityScenarioLauncherRule();

    @Before
    public void setUp() throws Exception {
        ShadowApplication shadowApplication = shadowOf(ApplicationProvider.<Application>getApplicationContext());
        shadowApplication.grantPermissions("android.permission.ACCESS_FINE_LOCATION");
        shadowApplication.grantPermissions("android.permission.ACCESS_COARSE_LOCATION");

        RobolectricApplication application = ApplicationProvider.getApplicationContext();
        application.geoDependencyComponent = DaggerGeoDependencyComponent.builder()
                .application(application)
                .geoDependencyModule(new GeoDependencyModule() {
                    @NonNull
                    @Override
                    public MapFragmentFactory providesMapFragmentFactory() {
                        return () -> mapFragment;
                    }

                    @NonNull
                    @Override
                    public ReferenceLayerSettingsNavigator providesReferenceLayerSettingsNavigator() {
                        return (activity) -> {};
                    }

                    @NonNull
                    @Override
                    public LocationTracker providesLocationTracker(@NonNull Application application) {
                        return locationTracker;
                    }
                })
                .build();
    }

    @Test
    public void testLocationTrackerLifecycle() {
        ActivityScenario<GeoPolyActivity> scenario = launcherRule.launch(GeoPolyActivity.class);
        mapFragment.ready();

        // Stopping the activity should stop the location tracker
        scenario.moveToState(Lifecycle.State.DESTROYED);
        verify(locationTracker).stop();
    }

    @Test
    public void recordButton_should_beHiddenForAutomaticMode() {
        ActivityScenario<GeoPolyActivity> scenario = launcherRule.launch(GeoPolyActivity.class);
        mapFragment.ready();

        scenario.onActivity((activity -> {
            activity.updateRecordingMode(R.id.automatic_mode);
            activity.startInput();
            assertThat(activity.findViewById(R.id.record_button).getVisibility(), is(View.GONE));
        }));
    }

    @Test
    public void recordButton_should_beVisibleForManualMode() {
        ActivityScenario<GeoPolyActivity> scenario = launcherRule.launch(GeoPolyActivity.class);
        mapFragment.ready();

        scenario.onActivity((activity -> {
            activity.updateRecordingMode(R.id.manual_mode);
            activity.startInput();
            assertThat(activity.findViewById(R.id.record_button).getVisibility(), is(View.VISIBLE));
        }));
    }

    @Test
    public void startingInput_usingAutomaticMode_usesRetainMockAccuracyToStartLocationTracker() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), GeoPolyActivity.class);

        intent.putExtra(Constants.EXTRA_RETAIN_MOCK_ACCURACY, true);
        launcherRule.<GeoPolyActivity>launch(intent).onActivity(activity -> {
            mapFragment.ready();
            activity.updateRecordingMode(R.id.automatic_mode);
            activity.startInput();
            verify(locationTracker).start(true);
        });

        Mockito.reset(locationTracker); // Ignore previous calls

        intent.putExtra(Constants.EXTRA_RETAIN_MOCK_ACCURACY, false);
        launcherRule.<GeoPolyActivity>launch(intent).onActivity(activity -> {
            mapFragment.ready();
            activity.updateRecordingMode(R.id.automatic_mode);
            activity.startInput();
            verify(locationTracker).start(false);
        });
    }
}
