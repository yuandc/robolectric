package org.robolectric.shadows;

import android.app.Activity;
import android.content.Context;
import android.preference.EditTextPreference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;
import org.robolectric.tester.android.util.TestAttributeSet;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class EditTextPreferenceTest {

	private static final String SOME_TEXT = "some text";
	private EditTextPreference preference;

	private Context context;
	private TestAttributeSet attrs;

	@Before
	public void setup() {
		context = new Activity();
		preference = new EditTextPreference(context, attrs);
	}

	@Test
	public void testConstructor() {
		preference = new EditTextPreference(context, attrs, 7);
		assertNotNull(preference.getEditText());
	}

	@Test
	public void testSetText() {
		preference.setText(SOME_TEXT);
		assertThat((String) preference.getEditText().getText().toString(), equalTo(SOME_TEXT));
	}

}
