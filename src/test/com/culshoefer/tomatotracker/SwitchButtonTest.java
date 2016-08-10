package com.culshoefer.tomatotracker;

import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 31/07/16.
 */
public class SwitchButtonTest {

  SwitchButton sb;

  @Before
  public void setUp() throws Exception {
    JFXPanel runtime = new JFXPanel();
    this.sb = new SwitchButton();
  }

  @Test
  public void isTurnedOnByDefault() throws Exception {
    assertThat(sb.getTurnedOn().get(), is(true));
  }

}