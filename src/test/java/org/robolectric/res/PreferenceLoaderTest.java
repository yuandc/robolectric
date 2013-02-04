package org.robolectric.res;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;
import org.robolectric.util.I18nException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.robolectric.util.TestUtil.*;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PreferenceLoaderTest {
    private PreferenceLoader prefLoader;

    @Before
    public void setUp() throws Exception {
        ResourceExtractor resourceExtractor = new ResourceExtractor(testResources(), systemResources());
        prefLoader = new PreferenceLoader(resourceExtractor);
        new DocumentLoader(prefLoader).loadResourceXmlDir(testResources(), "xml");
    }

    @Test
    public void shouldCreateCorrectClasses() {
        PreferenceScreen screen = prefLoader.inflatePreferences(new Activity(), TEST_PACKAGE + ":xml/preferences");
        assertThatScreenMatchesExpected(screen);
    }

    @Test
    public void shouldLoadByResourceId() {
       PreferenceScreen screen = prefLoader.inflatePreferences(new Activity(), R.xml.preferences);
       assertThatScreenMatchesExpected(screen);
    }

    @Test(expected=I18nException.class)
    public void shouldThrowI18nExceptionOnPrefsWithBareStrings() throws Exception {
        Robolectric.getShadowApplication().setStrictI18n(true);
        ResourceExtractor resourceExtractor = new ResourceExtractor(testResources());

        prefLoader = new PreferenceLoader(resourceExtractor);
        new DocumentLoader(prefLoader).loadResourceXmlDir(testResources(), "xml");

        prefLoader.inflatePreferences(Robolectric.application, R.xml.preferences);
    }

    protected void assertThatScreenMatchesExpected(PreferenceScreen screen) {
        assertThat(screen.getPreferenceCount(), equalTo(7));

        assertThat(screen.getPreference(0), instanceOf(PreferenceCategory.class));
        assertThat(((PreferenceCategory)screen.getPreference(0)).getPreference(0), instanceOf(Preference.class));

        PreferenceScreen innerScreen = (PreferenceScreen) screen.getPreference(1);
        assertThat(innerScreen, instanceOf(PreferenceScreen.class));
        assertThat(innerScreen.getKey().toString(), is("screen"));
        assertThat(innerScreen.getTitle().toString(), is("Screen Test"));
        assertThat(innerScreen.getSummary(), nullValue());
        assertThat(innerScreen.getPreference(0), instanceOf(Preference.class));

        assertThat(screen.getPreference(2), instanceOf(CheckBoxPreference.class));
        assertThat(screen.getPreference(3), instanceOf(EditTextPreference.class));
        assertThat(screen.getPreference(4), instanceOf(ListPreference.class));
        assertThat(screen.getPreference(5), instanceOf(Preference.class));
        assertThat(screen.getPreference(6), instanceOf(RingtonePreference.class));
    }
}
