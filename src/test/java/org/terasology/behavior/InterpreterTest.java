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
package org.terasology.behavior;

import org.junit.Test;
import org.terasology.behavior.tree.Interpreter;
import org.terasology.behavior.tree.Node;
import org.terasology.behavior.tree.Status;
import org.terasology.behavior.tree.Task;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


/**
 * @author synopia
 */
public class InterpreterTest {
    private Status result = Status.RUNNING;
    private Node node;
    private Task task;

    @Test
    public void testInit() {
        create();
        Interpreter interpreter = new Interpreter(null);
        interpreter.start(node);
        interpreter.tick(0);
        verify(task).onInitialize();
    }

    @Test
    public void testUpdate() {
        create();
        Interpreter interpreter = new Interpreter(null);
        interpreter.start(node);
        interpreter.tick(0);
        verify(task).update(anyInt());
    }

    @Test
    public void testNoTerminate() {
        create();
        Interpreter interpreter = new Interpreter(null);
        interpreter.start(node);
        interpreter.tick(0);
        verify(task, never()).onTerminate(any(Status.class));
    }

    @Test
    public void testTerminate() {
        create();
        Interpreter interpreter = new Interpreter(null);
        interpreter.start(node);
        interpreter.tick(0);
        result = Status.SUCCESS;
        interpreter.tick(0);
        verify(task).onTerminate(Status.SUCCESS);
    }

    private void create() {
        node = new Node() {
            @Override
            public Task create() {
                task = spy(new Task(null) {
                    @Override
                    public Status update(float dt) {
                        return result;
                    }
                });
                return task;
            }
        };
    }
}