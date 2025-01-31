/*
    Copyright 2019 Ryan Hoffman
    GPLv3 - see LICENSE file in project root

    Forked from:

    ---------------------------------------------------------------------

    Copyright 2018 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.

 */
package dev.rhoffman.cnc.joypadjog;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.willwinder.universalgcodesender.uielements.helpers.MachineStatusFontManager;
import com.willwinder.universalgcodesender.uielements.helpers.SteppedSizeManager;
import dev.rhoffman.gamepadevents.ConnectionEvent;
import net.miginfocom.swing.MigLayout;
import org.openide.util.ImageUtilities;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.HashSet;
import java.util.logging.*;
import java.util.Set;

/**
 * A panel for displaying jog controls
 *
 * @author Joacim Breiler
 */
public class JoyPanel extends JPanel implements SteppedSizeManager.SteppedSizeChangeListener, ChangeListener {

    /**
     * The minimum width and height of the jog buttons.
     */
    private static final int MINIMUM_BUTTON_SIZE = 52;

    private static final float FONT_SIZE_LABEL_SMALL = 8;
    private static final float FONT_SIZE_LABEL_MEDIUM = 10;
    private static final float FONT_SIZE_LABEL_LARGE = 14;

    /**
     * A list of listeners
     */
    private final Set<JoyPanelListener> listeners = new HashSet<>();

    /**
     * A map with all buttons and labels that allows bi-directional lookups with key->value and value->key
     */
    private final BiMap<JoyPanelUIEnum, JButton> buttons = HashBiMap.create();
    private final BiMap<JoyPanelUIEnum, JLabel> labels = HashBiMap.create();

    private final Logger logger = Logger.getLogger(this.getClass().getName());

//    /**
//     * Spinners for jog settings
//     */
//    private StepSizeSpinner zStepSizeSpinner;
//    private StepSizeSpinner feedRateSpinner;
//    private StepSizeSpinner xyStepSizeSpinner;

    public JoyPanel() {
        createComponents();
        initPanels();
        initListeners();
    }

    private static boolean isDarkLaF() {
        return UIManager.getBoolean("nb.dark.theme"); //NOI18N
    }

    private void createComponents() {

        String fontPath = "/resources/";
        // https://www.fontsquirrel.com
        String fontName = "OpenSans-Regular.ttf";
        InputStream is = getClass().getResourceAsStream(fontPath + fontName);
        Font font = MachineStatusFontManager.createFont(is, fontName).deriveFont(Font.PLAIN, FONT_SIZE_LABEL_LARGE);

        buttons.put(JoyPanelUIEnum.BUTTON_JOYPAD_SEARCH, new JButton("Search for Joypad"));
        labels.put(JoyPanelUIEnum.LABEL_CONNECTION_STATE, new JLabel("Not connected " + java.time.LocalTime.now().toString()));

    }

    private JLabel createSettingLabel(Font font, String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(font);
        return label;
    }

//    public void setFeedRate(int feedRate) {
//        feedRateSpinner.setValue(String.valueOf(feedRate));
//    }
//
//    public void setStepSizeXY(double stepSize) {
//        xyStepSizeSpinner.setValue(stepSize);
//    }
//
//    public void setStepSizeZ(double stepSize) {
//        zStepSizeSpinner.setValue(stepSize);
//    }
//
//    public void setUnit(UnitUtils.Units unit) {
//        getButtonFromEnum(JoyPanelButtonEnum.BUTTON_TOGGLE_UNIT).setText(unit.name());
//    }

//    public void setUseStepSizeZ(boolean useStepSizeZ) {
//        if (useStepSizeZ) {
//            // zStepLabel.setVisible(true);
//            zStepSizeSpinner.setVisible(true);
//            // xyStepLabel.setText(Localization.getString("platform.plugin.jog.stepSizeXY").toUpperCase());
//        } else {
//            // zStepLabel.setVisible(false);
//            zStepSizeSpinner.setVisible(false);
//            // xyStepLabel.setText(Localization.getString("platform.plugin.jog.stepSize").toUpperCase());
//        }
//    }

    private void initPanels() {
        setLayout(new MigLayout("fill, inset 5, gap 7"));
        add(createConfigurationPanel(), "grow, wrap");
    }

    private JPanel createConfigurationPanel() {
        JPanel configurationPanel = new JPanel();

        configurationPanel.add(buttons.get(JoyPanelUIEnum.BUTTON_JOYPAD_SEARCH));
        configurationPanel.add(labels.get(JoyPanelUIEnum.LABEL_CONNECTION_STATE), "growx, wrap");

        return configurationPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();



        return infoPanel;
    }


    private void initListeners() {

        // Creates a window size listener
        SteppedSizeManager sizer = new SteppedSizeManager(this,
                new Dimension(230, 0), // Scaling fonts to extra small
                new Dimension(250, 0)  // Scaling fonts to small
        );
        sizer.addListener(this);

        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logger.log(Level.INFO, "Mouse click event from " + getButtonEnumFromMouseEvent(e).toString());
                if(!SwingUtilities.isLeftMouseButton(e)) {
                    return; // ignore RMB
                }
                JoyPanelUIEnum buttonEnum = getButtonEnumFromMouseEvent(e);
                listeners.forEach(a -> a.onButtonClicked(buttonEnum));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        buttons.values().forEach(b -> b.addMouseListener(mouseListener));

//        xyStepSizeSpinner.addChangeListener(this);
//        zStepSizeSpinner.addChangeListener(this);
//        feedRateSpinner.addChangeListener(this);
    }

    /**
     * Finds the button enum based on the mouse event source
     *
     * @param mouseEvent the event that we want to extract the button enum from
     * @return the enum for the button
     */
    private JoyPanelUIEnum getButtonEnumFromMouseEvent(MouseEvent mouseEvent) {
        JButton releasedButton = (JButton) mouseEvent.getSource();
        return buttons.inverse().get(releasedButton);
    }

    /**
     * Returns the button from the button map using a button enum
     *
     * @param buttonEnum the button enum
     * @return the button
     */
    private JButton getButtonFromEnum(JoyPanelUIEnum buttonEnum) throws Exception {
        if (buttons.get(buttonEnum) != null) {
            return buttons.get(buttonEnum);
        } else {
            throw new Exception("that isn't a button!");
        }
    }


    /**
     * Creates a image button with a text.
     *
     * @param baseUri            the base uri of the image
     * @param text               the text to be shown togheter with the icon
     * @param verticalAligment   Sets the vertical position of the text relative to the icon
     *                           and can have one of the following values
     *                           <ul>
     *                           <li>{@code SwingConstants.CENTER} (the default)
     *                           <li>{@code SwingConstants.TOP}
     *                           <li>{@code SwingConstants.BOTTOM}
     *                           </ul>
     * @param horisontalAligment Sets the horizontal position of the text relative to the
     *                           icon and can have one of the following values:
     *                           <ul>
     *                           <li>{@code SwingConstants.RIGHT}
     *                           <li>{@code SwingConstants.LEFT}
     *                           <li>{@code SwingConstants.CENTER}
     *                           <li>{@code SwingConstants.LEADING}
     *                           <li>{@code SwingConstants.TRAILING} (the default)
     *                           </ul>
     * @return the button
     */
    private JButton createImageButton(String baseUri, String text, int verticalAligment, int horisontalAligment) {
        JButton button = createImageButton(baseUri);
        button.setText(text);
        button.setVerticalTextPosition(verticalAligment);
        button.setHorizontalTextPosition(horisontalAligment);
        return button;
    }

    /**
     * Creates a image button.
     *
     * @param baseUri the base uri of the image
     * @return the button
     */
    private JButton createImageButton(String baseUri) {
        ImageIcon imageIcon = ImageUtilities.loadImageIcon(baseUri, false);
        JButton button = new JButton(imageIcon);
        button.setMinimumSize(new Dimension(MINIMUM_BUTTON_SIZE, MINIMUM_BUTTON_SIZE));
        button.setFocusable(false);
        return button;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
//        buttons.values().forEach(button -> button.setEnabled(enabled));
//
//        xyStepSizeSpinner.setEnabled(enabled);
//        zStepSizeSpinner.setEnabled(enabled);
//        feedRateSpinner.setEnabled(enabled);
//
//        xyStepLabel.setEnabled(enabled);
//        zStepLabel.setEnabled(enabled);
//        feedRateLabel.setEnabled(enabled);
    }

    @Override
    public void onSizeChange(int size) {
        switch (size) {
            case 0:
                setFontSizeExtraSmall();
                break;
            case 1:
                setFontSizeSmall();
                break;
            default:
                setFontSizeNormal();
                break;
        }
    }

    private void setFontSizeExtraSmall() {
//        JButton unitToggleButton = getButtonFromEnum(JoyPanelUIEnum.BUTTON_TOGGLE_UNIT);
//        Font font = unitToggleButton.getFont().deriveFont(FONT_SIZE_LABEL_MEDIUM);
//        unitToggleButton.setFont(font);
//
//        font = this.feedRateLabel.getFont().deriveFont(FONT_SIZE_LABEL_SMALL);
//        this.feedRateLabel.setFont(font);
//
//        font = this.xyStepLabel.getFont().deriveFont(FONT_SIZE_LABEL_SMALL);
//        this.xyStepLabel.setFont(font);
//
//        font = this.zStepLabel.getFont().deriveFont(FONT_SIZE_LABEL_SMALL);
//        this.zStepLabel.setFont(font);
    }

    private void setFontSizeSmall() {
//        JButton unitToggleButton = getButtonFromEnum(JoyPanelUIEnum.BUTTON_TOGGLE_UNIT);
//        Font font = unitToggleButton.getFont().deriveFont(FONT_SIZE_LABEL_MEDIUM);
//        unitToggleButton.setFont(font);
//
//        font = this.feedRateLabel.getFont().deriveFont(FONT_SIZE_LABEL_MEDIUM);
//        this.feedRateLabel.setFont(font);
//
//        font = this.xyStepLabel.getFont().deriveFont(FONT_SIZE_LABEL_MEDIUM);
//        this.xyStepLabel.setFont(font);
//
//        font = this.zStepLabel.getFont().deriveFont(FONT_SIZE_LABEL_MEDIUM);
//        this.zStepLabel.setFont(font);
    }

    private void setFontSizeNormal() {
//        JButton unitToggleButton = getButtonFromEnum(JoyPanelUIEnum.BUTTON_TOGGLE_UNIT);
//        Font font = unitToggleButton.getFont().deriveFont(FONT_SIZE_LABEL_MEDIUM);
//        unitToggleButton.setFont(font);
//
//        font = this.feedRateLabel.getFont().deriveFont(FONT_SIZE_LABEL_LARGE);
//        this.feedRateLabel.setFont(font);
//
//        font = this.xyStepLabel.getFont().deriveFont(FONT_SIZE_LABEL_LARGE);
//        this.xyStepLabel.setFont(font);
//
//        font = this.zStepLabel.getFont().deriveFont(FONT_SIZE_LABEL_LARGE);
//        this.zStepLabel.setFont(font);
    }

    public void addListener(JoyPanelListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
//        if (e.getSource() == zStepSizeSpinner) {
//            this.listeners.forEach(listener -> listener.onStepSizeZChanged(zStepSizeSpinner.getValue()));
//        } else if (e.getSource() == xyStepSizeSpinner) {
//            this.listeners.forEach(listener -> listener.onStepSizeXYChanged(xyStepSizeSpinner.getValue()));
//        } else if (e.getSource() == feedRateSpinner) {
//            this.listeners.forEach(listener -> listener.onFeedRateChanged(feedRateSpinner.getValue().intValue()));
//        }
    }

    public void setConnectionLabel(ConnectionEvent.Type type) {
        switch (type) {
            case CONNECTED:
                labels.get(JoyPanelUIEnum.LABEL_CONNECTION_STATE).setText("Connected");
                break;
            case DISCONNECTED:
                labels.get(JoyPanelUIEnum.LABEL_CONNECTION_STATE).setText("DISCONNECTED!");
                break;
        }
    }
}
