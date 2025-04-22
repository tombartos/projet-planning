package fr.univtln.m1im.png.model;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GroupeTest {
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

    @Test
    void testAcyclic() {
        var g1 = Groupe.builder().build();
        var g2 = Groupe.builder().parent(g1).build();
        var g3 = Groupe.builder().parent(g2).build();

        assertThrows(IllegalArgumentException.class, () -> g1.setParent(g3));
    }

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

    @Test
    void testDefaultBuilder() {
        var g = Groupe.builder().build();

        assertTrue(g.getModules().isEmpty());
        assertTrue(g.getCreneaux().isEmpty());
        assertTrue(g.getEtudiants().isEmpty());
        assertTrue(g.getSousGroupes().isEmpty());
    }
}
