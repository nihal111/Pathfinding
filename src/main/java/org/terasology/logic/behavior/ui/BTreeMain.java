/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.logic.behavior.ui;

import org.terasology.logic.behavior.BehaviorFactory;
import org.terasology.logic.behavior.tree.Actor;
import org.terasology.logic.behavior.tree.Interpreter;

import javax.swing.*;
import java.awt.*;

/**
 * @author synopia
 */
public class BTreeMain extends JPanel {

    private BTreePanel panel;
    private Debugger debugger;

    public BTreeMain(BehaviorFactory factory) throws HeadlessException {
        setLayout(new BorderLayout());
        panel = new BTreePanel();
        panel.setFactory(factory);
        JToolBar bar = new JToolBar();
        debugger = new Debugger();
        bar.add(debugger);
        bar.add(panel.createToolBar());
        add(bar, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        panel.init();
    }

    public void setInterpreter(Interpreter interpreter) {
        panel.setInterpreter(interpreter);
        debugger.setInterpreter(interpreter);
    }

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter(new Actor(null));
        BehaviorFactory factory = new BehaviorFactory();
        RenderableNode root;
        try {
            root = factory.addNode(factory.get(""));
//            root = factory.load(new FileInputStream("test.json"));
            interpreter.start(root.getNode());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        JFrame frame = new JFrame();
        BTreeMain main = new BTreeMain(factory);
        main.setInterpreter(interpreter);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(main);
        frame.pack();
        frame.setVisible(true);
    }
}