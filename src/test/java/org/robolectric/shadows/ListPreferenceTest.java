package org.robolectric.shadows;

import android.app.Activity;
import android.preference.ListPreference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class ListPreferenceTest {

	private ListPreference listPreference;
	private ShadowListPreference shadow;

	@Before
	public void setUp() throws Exception {
		listPreference = new ListPreference(new Activity());
		shadow = Robolectric.shadowOf(listPreference);
    }
    
	@Test
	public void shouldInheritFromDialogPreference() {
		assertThat(shadow, instanceOf(ShadowDialogPreference.class));
	}	
	
	@Test
	public void shouldHaveEntries() {
		CharSequence[] entries = { "this", "is", "only", "a", "test" };
		
		assertThat(listPreference.getEntries(), nullValue());
		listPreference.setEntries(entries);
		assertThat(listPreference.getEntries(), sameInstance(entries));		
	}
	
	@Test
	public void shouldSetEntriesByResourceId() {
		assertThat(listPreference.getEntries(), nullValue());
		listPreference.setEntries(R.array.greetings);
		assertThat(listPreference.getEntries(), notNullValue());			
	}
	
	@Test
	public void shouldHaveEntryValues() {
		CharSequence[] entryValues = { "this", "is", "only", "a", "test" };
		
		assertThat(listPreference.getEntryValues(), nullValue());
		listPreference.setEntryValues(entryValues);
		assertThat(listPreference.getEntryValues(), sameInstance(entryValues));		
	}
	
	@Test
	public void shouldSetEntryValuesByResourceId() {
		assertThat(listPreference.getEntryValues(), nullValue());
		listPreference.setEntryValues(R.array.greetings);
		assertThat(listPreference.getEntryValues(), notNullValue());			
	}
	
	@Test
	public void shouldSetValue() {
		assertThat(listPreference.getValue(), nullValue());
		listPreference.setValue("testing");
		assertThat(listPreference.getValue(), equalTo("testing"));
	}
}
