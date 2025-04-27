package fr.univtln.m1im.png.model;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Groupe} class.
 */
class GroupeTest {

    /**
     * Tests the creation of a parent group and its relationship with a child group.
     */
    @Test
    void testBuildParentGroup() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().parent(g1).build();

        assertNull(g1.getParent());
        assertEquals(g1, g2.getParent());

        int count = 0;
        for (var g : g1.getSousGroupes()) {
            assertEquals(g2, g);
            count += 1;
        }
        assertEquals(1, count);
        assertEquals(count, g1.getSousGroupes().size());
    }

    /**
     * Tests setting a parent group for a group.
     */
    @Test
    void testSetParentGroup() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().build();

        assertNull(g1.getParent());
        assertNull(g2.getParent());
        assertTrue(g1.getSousGroupes().isEmpty());
        assertTrue(g2.getSousGroupes().isEmpty());

        g2.setParent(g1);

        assertNull(g1.getParent());
        assertEquals(g1, g2.getParent());

        int count = 0;
        for (var g : g1.getSousGroupes()) {
            assertEquals(g2, g);
            count += 1;
        }
        assertEquals(1, count);
        assertEquals(count, g1.getSousGroupes().size());
    }

    /**
     * Tests unsetting the parent group of a group.
     */
    @Test
    void testUnsetParentGroup() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().parent(g1).build();

        assertFalse(g1.getSousGroupes().isEmpty());
        assertEquals(g1, g2.getParent());

        g2.setParent(null);

        assertThrows(NoSuchElementException.class, () -> g1.getSousGroupes().getFirst());
        assertTrue(g1.getSousGroupes().isEmpty());
        assertNull(g2.getParent());
    }

    /**
     * Tests if a group is a descendant of another group.
     */
    @Test
    void testIsDescendantOf() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().parent(g1).build();
        var g3 = Groupe.builder().parent(g2).build();

        assertTrue(g1.isDescendantOf(g1));
        assertTrue(g2.isDescendantOf(g2));
        assertTrue(g3.isDescendantOf(g3));

        assertTrue(g3.isDescendantOf(g1));
        assertTrue(g3.isDescendantOf(g2));
        assertTrue(g2.isDescendantOf(g1));

        assertFalse(g1.isDescendantOf(g3));
        assertFalse(g1.isDescendantOf(g2));
        assertFalse(g2.isDescendantOf(g3));

        assertFalse(g1.isDescendantOf(null));
    }

    /**
     * Tests that the group hierarchy remains acyclic.
     */
    @Test
    void testAcyclic() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().parent(g1).build();
        var g3 = Groupe.builder().parent(g2).build();

        assertThrows(IllegalArgumentException.class, () -> g1.setParent(g3));
    }

    /**
     * Tests adding multiple children to a parent group.
     */
    @Test
    void testTwoChildren() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().parent(g1).build();
        var g3 = Groupe.builder().build();

        g3.setParent(g1);

        assertEquals(g1, g2.getParent());
        assertEquals(g1, g3.getParent());
        assertTrue(g1.getSousGroupes().contains(g2));
        assertTrue(g1.getSousGroupes().contains(g3));
    }

    /**
     * Tests moving a group to a new parent group.
     */
    @Test
    void testMoveToParent() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().parent(g1).build();
        var g3 = Groupe.builder().parent(g2).build();

        g3.setParent(g1);

        assertEquals(g1, g2.getParent());
        assertEquals(g1, g3.getParent());
        assertTrue(g1.getSousGroupes().contains(g2));
        assertTrue(g1.getSousGroupes().contains(g3));
    }

    /**
     * Tests the default builder configuration for a group.
     */
    @Test
    void testDefaultBuilder() {
        var g = Groupe.builder().build();

        assertTrue(g.getModules().isEmpty());
        assertTrue(g.getCreneaux().isEmpty());
        assertTrue(g.getEtudiants().isEmpty());
        assertTrue(g.getSousGroupes().isEmpty());
    }

    /**
     * Tests the iteration of modules in a group hierarchy.
     */
    @Test
    void testModuleIteration() {
        var m1 = Module.builder().code("M1").build();
        var m2 = Module.builder().code("M2").build();
        var m3 = Module.builder().code("M3").build();
        var m4 = Module.builder().code("M4").build();
        var m5 = Module.builder().code("M5").build();

        var g1 = Groupe.builder().modules(List.of(m1, m2)).build();
        var g2 = Groupe.builder().parent(g1).modules(List.of(m3)).build();
        var g3 = Groupe.builder().parent(g2).modules(List.of(m4, m5)).build();

        assertEquals(5, g3.getModules().size());

        java.util.Iterator<Module> iter = g3.getModules().iterator();
        for (var m : List.of(m4, m5, m3, m1, m2)) {
            assertTrue(iter.hasNext());
            assertEquals(m, iter.next());
        }
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    /**
     * Tests the indexing of modules in a group hierarchy.
     */
    @Test
    void testModuleIndexation() {
        var m1 = Module.builder().code("M1").build();
        var m2 = Module.builder().code("M2").build();
        var m3 = Module.builder().code("M3").build();
        var m4 = Module.builder().code("M4").build();
        var m5 = Module.builder().code("M5").build();

        var g1 = Groupe.builder().modules(List.of(m1, m2)).build();
        var g2 = Groupe.builder().parent(g1).modules(List.of(m3)).build();
        var g3 = Groupe.builder().parent(g2).modules(List.of(m4, m5)).build();

        assertEquals(5, g3.getModules().size());
        assertEquals(m4, g3.getModules().get(0));
        assertEquals(m5, g3.getModules().get(1));
        assertEquals(m3, g3.getModules().get(2));
        assertEquals(m1, g3.getModules().get(3));
        assertEquals(m2, g3.getModules().get(4));
        assertThrows(IndexOutOfBoundsException.class, () -> g3.getModules().get(5));
    }
}
