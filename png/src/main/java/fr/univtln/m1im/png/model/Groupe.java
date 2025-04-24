package fr.univtln.m1im.png.model;

import jakarta.persistence.Column;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Table;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Entity
@Table(name = "Groupes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@NamedQueries({
        @NamedQuery(name = "Groupe.getAll", query = "SELECT g FROM Groupe g"),
        @NamedQuery(name = "Groupe.getAllDTO", query = "SELECT new fr.univtln.m1im.png.dto.GroupeDTO(g.code, g.nom, g.formation) FROM Groupe g"),
        @NamedQuery(name = "Groupe.getWeekCreneaux", query = "SELECT c FROM Groupe g JOIN g.creneaux c WHERE g.code = :code AND c.heureDebut BETWEEN :firstDay AND :lastDay"),
        @NamedQuery(name = "Groupe.getByCode", query = "SELECT g FROM Groupe g WHERE g.code = :code")
})
public class Groupe {
    @Id
    private String code;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String formation;

    @ManyToOne
    @Access(AccessType.FIELD)
    private Groupe parent;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Etudiant> etudiants = new ArrayList<Etudiant>();

    @ToString.Exclude
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Groupe> sousGroupes = new ArrayList<Groupe>(); // Sous groupes en structure d'arbre

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Module> modules;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Creneau> creneaux = new ArrayList<Creneau>();

    @Builder
    Groupe(String code, String nom, String formation, Groupe parent, List<Module> modules) {
        this.code = code;
        this.nom = nom;
        this.formation = formation;
        this.parent = parent;
        this.modules = modules;

        if (parent != null)
            parent.sousGroupes.add(this);
    }

    public static class GroupeBuilder {
        private List<Module> modules = new ArrayList<Module>();
    }

    public void setParent(Groupe parent) {
        if (parent == null) {
            this.parent.sousGroupes.remove(this);
        } else if (parent.isDescendantOf(this)) {
            throw new IllegalArgumentException("Setting parent to a descendant creates a cycle.");
        } else {
            parent.sousGroupes.add(this);
        }
        this.parent = parent;
    }

    public InheritanceList<Module> getModules() {
        return InheritanceList.moduleList(this);
    }

    public static class InheritanceList<T> extends AbstractList<T> {
        private final Groupe firstGroupe;
        private final Function<Groupe, List<T>> attrGetter;

        private InheritanceList(Groupe groupe, Function<Groupe, List<T>> attr) {
            this.firstGroupe = groupe;
            this.attrGetter = attr;
        }

        static InheritanceList<Module> moduleList(Groupe gr) {
            return new InheritanceList<>(gr, g -> g.modules);
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private Groupe groupe = firstGroupe;
                private Iterator<T> iter = attrGetter.apply(groupe).iterator();

                @Override
                public boolean hasNext() {
                    return iter.hasNext() ||
                        groupe.parent != null &&
                        attrGetter.apply(groupe.parent).iterator().hasNext();
                }

                @Override
                public T next() {
                    if (iter.hasNext() || groupe.parent == null) {
                        return iter.next();
                    } else {
                        groupe = groupe.parent;
                        iter = attrGetter.apply(groupe).iterator();
                        return iter.next();
                    }
                }
            };
        }

        @Override
        public int size() {
            int size = 0;
            for (var groupe = firstGroupe; groupe != null; groupe = groupe.getParent()) {
                size += attrGetter.apply(groupe).size();
            }
            return size;
        }

        @Override
        public T get(int i) {
            for (var groupe = firstGroupe; groupe != null; groupe = groupe.getParent()) {
                var list = attrGetter.apply(groupe);
                if (i < list.size()) {
                    return list.get(i);
                } else {
                    i -= list.size();
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Adds a student to the current group and propagates the addition to the parent
     * group if it exists, add the group to etudiant too.
     *
     * @param etudiant the student to be added to the group
     */
    public void addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getGroupes().add(this);
        if (parent != null) {
            parent.addEtudiant(etudiant);
        }
    }

    /**
     * Adds a Creneau (time slot) to the current group and propagates the addition
     * to the parent group if it exists. WARNING: this method does not add the
     * Groupe to the Creneau object.
     *
     * @param creneau the Creneau object to be added to the group
     */
    // public void addCreneau(Creneau creneau) {
    // this.creneaux.add(creneau);
    // if (parent != null) {
    // parent.addCreneau(creneau);
    // }
    // }

    public boolean isDescendantOf(Groupe other) {
        if (this.equals(other)) {
            return true;
        } else if (parent == null) {
            return false;
        } else {
            return parent.isDescendantOf(other);
        }
    }

    public void forEachFeuil(Consumer<Groupe> consumer) {
        if (sousGroupes.isEmpty()) {
            consumer.accept(this);
        } else {
            for (var enfant : sousGroupes) {
                enfant.forEachFeuil(consumer);
            }
        }
    }

    public void forEachDescendant(Consumer<Groupe> consumer) {
        consumer.accept(this);
        for (var enfant : sousGroupes) {
            enfant.forEachDescendant(consumer);
        }
    }

    public void forEachAncetre(Consumer<Groupe> consumer) {
        for (var parent = this; parent != null; parent = parent.getParent()) {
            consumer.accept(parent);
        }
    }

    public Collection<Groupe> getAncetres() {
        final var first = this;
        return new AbstractCollection<>() {

            @Override
            public Iterator<Groupe> iterator() {
                return new Iterator<>() {
                    private Groupe next = first;

                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public Groupe next() {
                        final var groupe = next;
                        next = groupe.parent;
                        return groupe;
                    }
                };
            }

            @Override
            public int size() {
                var n = 0;
                for (var g = first; g != null; g = g.getParent()) ++n;
                return n;
            }
        };
    }

    public boolean isLeaf() {
        return sousGroupes.isEmpty();
    }
}
