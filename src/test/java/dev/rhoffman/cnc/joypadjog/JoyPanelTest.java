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


import javax.swing.*;

public class JoyPanelTest extends JFrame {
    private JoyPanel joyPanel;

    public static void main(String[] args) throws Exception {
        JoyPanelTest joyPanelTest = new JoyPanelTest();
        joyPanelTest.start();
    }

    private void start() throws Exception {
        joyPanel = new JoyPanel();
        getContentPane().add(joyPanel);

        createMenuBar();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);

        joyPanel.setFeedRate(1000);
        joyPanel.setStepSizeXY(100);
        joyPanel.setStepSizeZ(0.01);

        setMinimumSize(joyPanel.getMinimumSize());
    }

    private void createMenuBar() {
        JMenu fileMenu = new JMenu("File");
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        JMenuItem menuItem = new JMenuItem("Enabled");
        menuItem.addActionListener(e -> joyPanel.setEnabled(true));
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Disabled");
        menuItem.addActionListener(e -> joyPanel.setEnabled(false));
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Use Z step size");
        menuItem.addActionListener(e -> joyPanel.setUseStepSizeZ(true));
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Don't use Z step size");
        menuItem.addActionListener(e -> joyPanel.setUseStepSizeZ(false));
        fileMenu.add(menuItem);

        setJMenuBar(menuBar);
    }
}
