/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.ogust.dao;

import agrold.ogust.beans.AdvancedSearch;
import agrold.ogust.beans.QuickSearch;
import agrold.ogust.beans.SparqlEditor;
import agrold.ogust.handler.AjaxShell;
import java.util.Map;

/**
 *
 * @author Jc
 */
public interface HistoryDAO {
    /*
     * Récupération des Historiques
     */
    AjaxShell getQuickSearch(Object o);
    AjaxShell getAdvancedSearch(Object o);
    AjaxShell getSparqlEditor(Object o);
    /*
     * Suppression d'historiques {1...n}
     */
    AjaxShell deleteQuickSearch(Object o);
    AjaxShell deleteAdvancedSearch(Object o);
    AjaxShell deleteSparqlEditor(Object o);
    /**
     * Ajout d'un historique
     */
    AjaxShell setQuickSearch(Object o);
    AjaxShell setAdvancedSearch(Object o);
    AjaxShell setSparqlEditor(Object o);
    
}
