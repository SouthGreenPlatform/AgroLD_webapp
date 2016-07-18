#!/usr/bin/env python
'''
Created on Jun 4, 2014
The gafToRDF module is created as part of the Rice Knowledge Base project. 

Common parameters:
inputFileHandle - gaf file handle

outputfileHandle - RDF file handle

flag - is a string e.g. 'protein' or 'gene' or 'qtl', depending on the flag specific URIs are written

tag - default is used to parse GO association files only! It needs to be switched to False for parsing other 
ontology associations such as PO, TO etc...This is due to the relations used. 

Functions:

goaRDF - The functions parses gene/protein/qtl/ - ontology association files of the .gaf file format
to RDF files in Turtle sytax.
Note: Should be used to produce ontology specific RDF files

allGafRDF - The functions is similar to goaRDF except it parses all ontologies into a single  gene/protein/qtl/ specific RDF file.

Usage:
goa = gafToRDF
goa.goaRDF (inputFileHandle, outputfileHandle, flag, tag=True)

goa = gafToRDF
goa.allGafRDF (inputFileHandle, outputfileHandle, flag, tag=True)

@author: venkatesan
'''
'''
TODO: 1) Reification needs fixing - additional rdf statement required to link aspects to its corresponding reification statement
         check Bio2RDF goa dataset for this
      2) Streamlined usage of internal predicates
      3) Strictly follow RDF Data types, e.g: date must be change to 'date data type' which is currently a string literal 
      3) Cleanup code

'''

import re
from Bio.UniProt import GOA
from globalVars import *
import pprint
import glob
import os

'''
def goaRDF(files, output_file, flag):
    flag.lower()
#    assoc_line = 0
    rdf_buffer = '' 
    previous_obj_id = ''
#    current_obj_id = ''
#    uniq_obj_id = {}
    # Printing prefixes
    output_file.write(base + "\t" + "<" + base_uri + "> .\n")
    output_file.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    output_file.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    output_file.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    output_file.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    output_file.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    output_file.write(pr + "\t" + sio_ns + "<" + sio_uri + "> .\n")
    output_file.write(pr + "\t" + ncbi_tax_ns + "<" + ncbi_tax_uri + "> .\n")
    output_file.write(pr + "\t" + gr_assoc_ns + "<" + gr_assoc + "> .\n")
    if flag == 'protein':
        output_file.write(pr + "\t" + goa_ns + "<" + goa_uri + "> .\n")
        output_file.write(pr + "\t" + up_ns + "<" + uniprot + "> .\n\n")
    if flag == 'gene':
        output_file.write(pr + "\t" + gr_g_ns + "<" + gramene_gene + "> .\n\n")
    if flag == 'qtl':
        output_file.write(pr + "\t" + gr_qtl_ns + "<" + gramene_qtl + "> .\n\n")
      
    for file_path in files:
        handle = open(file_path, "r")
        goa = GOA.gafiterator(handle)
        uniq_obj_id = {}
        for assoc in goa:
            taxon = ''.join(assoc['Taxon_ID'])
            tax_id = taxon.lstrip('taxon:')
            if taxon_ids[tax_id]:
#                assoc_line += 1
                ont_term = assoc['GO_ID'].replace(":", "_")
                current_obj_id = assoc['DB_Object_ID']
                aspect = assoc['Aspect']
                # Flush
                if previous_obj_id and current_obj_id not in previous_obj_id:
                    rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
                    output_file.write(rdf_buffer)
                    rdf_buffer = ''
                
                if current_obj_id not in uniq_obj_id:
                    if flag == 'protein':
                        rdf_buffer += up_ns + current_obj_id + "\n"
                    if flag == 'gene':
                        rdf_buffer += gr_g_ns + current_obj_id + "\n"
                    if flag == 'qtl':
                        rdf_buffer += gr_qtl_ns + current_obj_id + "\n"
                    rdf_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (assoc['DB_Object_Symbol']) + " ;\n"
                    rdf_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (assoc['DB_Object_Name']) + " ;\n"
                    for synonym in assoc['Synonym']:
                        rdf_buffer += "\t" + base_vocab_ns + "has_synonym" + "\t" + '"%s"' % (synonym) + " ;\n"
                    rdf_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + ncbi_tax_ns + tax_id + " ;\n"
                    uniq_obj_id[current_obj_id] = 1
                    previous_obj_id = current_obj_id
                # Reification
                go_pattern = re.match(r'^GO', ont_term)
                if go_pattern:
                    output_file.write(goa_ns + current_obj_id + "\n")
                else:
                    output_file.write(gr_assoc_ns + current_obj_id + "_" + ont_term + "\n")
                output_file.write("\t" + rdf_ns + "type" + "\t" + rdf_ns +"Statement" + " ;\n")
                output_file.write("\t" + rdfs_ns + "subClassOf" + "\t" + sio_ns + sio_term + " ;\n")
                if flag == 'protein':
                    output_file.write("\t" + rdf_ns + "subject" + "\t" + up_ns + current_obj_id + " ;\n")
                if flag == 'gene':
                    output_file.write("\t" + rdf_ns + "subject" + "\t" + gr_g_ns + current_obj_id + " ;\n")
                if flag == 'qtl':
                    output_file.write("\t" + rdf_ns + "subject" + "\t" + gr_qtl_ns + current_obj_id + " ;\n") 
                if aspect in go_aspects:
                    output_file.write("\t" + rdf_ns + "predicate" + "\t" + obo_ns + go_aspects[aspect] + " ;\n")
                else:
                    output_file.write("\t" + rdf_ns + "predicate" + "\t" + base_ns + ont_aspects[aspect] + " ;\n")
                output_file.write("\t" + rdf_ns + "object" + "\t" + obo_ns + ont_term + " ;\n")
                output_file.write("\t" + base_vocab_ns + "has_evidence" + "\t" + '"%s"' % (assoc['Evidence']) + " ;\n")
                output_file.write("\t" + base_vocab_ns + "assigned_by" + "\t" + '"%s"' % (assoc['Assigned_By']) + " ;\n")
                output_file.write("\t" + base_vocab_ns + "date" + "\t" + '"%s"' % (assoc['Date']) + " .\n")
            
                # Flushing                
                if current_obj_id == previous_obj_id:
                    if aspect in go_aspects:
                        rdf_buffer += "\t" + obo_ns + go_aspects[aspect] + "\t" + obo_ns + ont_term + " ;\n"
                        rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + goa_ns + current_obj_id + " ;\n"
                    else:
                        rdf_buffer += "\t" + base_ns + ont_aspects[aspect] + "\t" + obo_ns + ont_term + " ;\n"
                        rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + gr_assoc_ns + current_obj_id + "_" + ont_term + " ;\n"
                previous_obj_id = current_obj_id
    #Last Flush
    if previous_obj_id:
        rdf_buffer = re.sub(' ;$', ' .', rdf_buffer) 
        output_file.write(rdf_buffer)
    handle.close()
'''
pp = pprint.PrettyPrinter(indent=4)


def allGafRDF(files, map_ds, output_file, flag):
    flag.lower()
    assoc_line = 0
    rdf_buffer = '' 
    previous_obj_id = ''
    list_records = list()
    uniq_obj_id = {}
#    pp = pprint.PrettyPrinter(indent=4)
#    if flag == 'protein' or flag == 'gene' or flag == 'qtl':
#        output_file += flag + "_associations.ttl"
    outputWriter = open(output_file, "w")
    # Printing prefixes
    outputWriter.write(base + "\t" + "<" + base_uri + "> .\n")
    outputWriter.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    outputWriter.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    outputWriter.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")    
    outputWriter.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    outputWriter.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    outputWriter.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    outputWriter.write(pr + "\t" + sio_ns + "<" + sio_uri + "> .\n")
    outputWriter.write(pr + "\t" + ncbi_tax_ns + "<" + ncbi_tax_uri + "> .\n")
    outputWriter.write(pr + "\t" + gr_assoc_ns + "<" + gr_assoc + "> .\n")
    if flag == 'protein':
        outputWriter.write(pr + "\t" + goa_ns + "<" + goa_uri + "> .\n")
        outputWriter.write(pr + "\t" + up_ns + "<" + uniprot + "> .\n\n")
#    if flag == 'gene':
#        outputWriter.write(pr + "\t" + goa_ns + "<" + goa_uri + "> .\n")
#        outputWriter.write(pr + "\t" + gr_g_ns + "<" + gramene_gene + "> .\n\n")
    if flag == 'qtl':
        outputWriter.write(pr + "\t" + gr_qtl_ns + "<" + gramene_qtl + "> .\n\n")
    
    # Slurping all the gaf records into gaf_objs list  
    for infile in files:

        opener = open(infile, "r")
        gaf_objs = GOA.gafiterator(opener)

        for record in gaf_objs:
            list_records.append(record) #append(record) extend(record)
        opener.close()
    #pp(list_records)
    pp.pprint(list_records)
    list_records.sort(key=lambda x: x['DB_Object_ID'])
#    pp.pprint(list_records)
    # Accessing individual associations
    for inline in list_records: 
        taxon = ''.join(inline['Taxon_ID'])
        tax_id = taxon.lstrip('taxon:')
#        d = inline['Date']
        date = inline['Date'][:4] + "-" + inline['Date'][4:6] + "-" + inline['Date'][6:]
        if tax_id not in taxon_ids:
            continue
        assoc_line += 1
        ont_term = inline['GO_ID'].replace(":", "_")
        current_obj_id = inline['DB_Object_ID']
        aspect = inline['Aspect']
        go_pattern = re.match(r'^GO', ont_term)
        evidence_code = inline['Evidence']
        db_ref = inline['DB:Reference']

        # Flush
        if previous_obj_id and current_obj_id not in previous_obj_id:
            rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
            outputWriter.write(rdf_buffer)
            rdf_buffer = ''
                
        if current_obj_id not in uniq_obj_id:
            if flag == 'protein':
                rdf_buffer += up_ns + current_obj_id + "\n"
                rdf_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + protein_term + " ;\n"
#            if flag == 'gene':
#                rdf_buffer += gr_g_ns + current_obj_id + "\n"
            if flag == 'qtl':
                rdf_buffer += gr_qtl_ns + current_obj_id + "\n"
                rdf_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + qtl_term + " ;\n"
            rdf_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (inline['DB_Object_Symbol']) + " ;\n"
            rdf_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (inline['DB_Object_Name']) + " ;\n"
            for synonym in inline['Synonym']:
                if synonym:
                    rdf_buffer += "\t" + base_vocab_ns + "has_synonym" + "\t" + '"%s"' % (synonym) + " ;\n"
            rdf_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + ncbi_tax_ns + tax_id + " ;\n"
            uniq_obj_id[current_obj_id] = 1
            previous_obj_id = current_obj_id
                
        # Reification
        if go_pattern:
#            outputWriter.write(goa_ns + current_obj_id + "_" + ont_term + "\n")
            outputWriter.write(goa_ns + current_obj_id + "\n")
        else:
            outputWriter.write(gr_assoc_ns + current_obj_id + "_" + ont_term + "\n")        
#        outputWriter.write(base_ns + "triple_" + current_obj_id + "_" + ont_term + "_" + str(assoc_line) + "\n")
        outputWriter.write("\t" + rdf_ns + "type" + "\t" + rdf_ns +"Statement" + " ;\n")
        outputWriter.write("\t" + rdfs_ns + "subClassOf" + "\t" + sio_ns + sio_term + " ;\n") 
        if flag == 'protein':
            outputWriter.write("\t" + rdf_ns + "subject" + "\t" + up_ns + current_obj_id + " ;\n")
#        if flag == 'gene':
#            outputWriter.write("\t" + rdf_ns + "subject" + "\t" + gr_g_ns + current_obj_id + " ;\n")
        if flag == 'qtl':
            outputWriter.write("\t" + rdf_ns + "subject" + "\t" + gr_qtl_ns + current_obj_id + " ;\n")
#        if aspect in go_aspects:
#            outputWriter.write("\t" + rdf_ns + "predicate" + "\t" + obo_ns + go_aspects[aspect] + " ;\n")
#        else:
#            outputWriter.write("\t" + rdf_ns + "predicate" + "\t" + base_vocab_ns + ont_aspects[aspect] + " ;\n")
        outputWriter.write("\t" + rdf_ns + "predicate" + "\t" + base_vocab_ns + ont_aspects[aspect] + " ;\n")
        outputWriter.write("\t" + rdf_ns + "object" + "\t" + obo_ns + ont_term + " ;\n")
        if evidence_code in map_ds:
            for db_ref in inline['DB:Reference']:
                if db_ref in map_ds[evidence_code]:
                    eco_id = map_ds[evidence_code][db_ref].replace(":", "_") 
                    outputWriter.write("\t" + base_vocab_ns + "evidence" + "\t" + obo_ns + eco_id + " ;\n")
                    outputWriter.write("\t" + base_vocab_ns + "evidence_code" + "\t" + '"%s"' % (evidence_code) + " ;\n")
                else:
                    eco_id = map_ds[evidence_code]['Default'].replace(":", "_")
                    outputWriter.write("\t" + base_vocab_ns + "evidence" + "\t" + obo_ns + eco_id + " ;\n")
                    outputWriter.write("\t" + base_vocab_ns + "evidence_code" + "\t" + '"%s"' % (evidence_code) + " ;\n")
        else:
            outputWriter.write("\t" + base_vocab_ns + "evidence" + "\t" + '"%s"' % (evidence_code) + " ;\n")
#        outputWriter.write("\t" + base_vocab_ns + "evidence" + "\t" + '"%s"' % (inline['Evidence']) + " ;\n")
        outputWriter.write("\t" + base_vocab_ns + "assigned_by" + "\t" + '"%s"' % (inline['Assigned_By']) + " ;\n")
        outputWriter.write("\t" + base_vocab_ns + "date" + "\t" + '"%s"' % (date) + "^^" + xsd_ns + "date" + " .\n")
                
        # Flushing                
        if current_obj_id == previous_obj_id:
            rdf_buffer += "\t" + base_vocab_ns + ont_aspects[aspect] + "\t" + obo_ns + ont_term + " ;\n"
            if go_pattern:
                rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + goa_ns + current_obj_id + " ;\n"
            else:
                rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + gr_assoc_ns + current_obj_id + "_" + ont_term + " ;\n"
#            if aspect in go_aspects:
#                rdf_buffer += "\t" + obo_ns + go_aspects[aspect] + "\t" + obo_ns + ont_term + " ;\n"
#                rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + goa_ns + current_obj_id + "_" + ont_term + " ;\n"
#            else:
#                rdf_buffer += "\t" + base_vocab_ns + ont_aspects[aspect] + "\t" + obo_ns + ont_term + " ;\n"
#                rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + gr_assoc_ns + current_obj_id + "_" + ont_term + " ;\n"
        previous_obj_id = current_obj_id
#        else:
#            break

    # Last Flush
    if previous_obj_id:
        rdf_buffer = re.sub(' ;$', ' .', rdf_buffer) 
        outputWriter.write(rdf_buffer)
    outputWriter.close()
    print "Total number of associations: %s\n" % (str(assoc_line))
#    print "Ontology associations has been converted to RDF!\n"
#    pp.pprint(uniq_obj_id) 

def ProteinGafRDF(files, map_ds, output_file):
    assoc_line = 0
    rdf_buffer = '' 
    previous_obj_id = ''
    list_records = list()
    uniq_obj_id = {}
    pp = pprint.PrettyPrinter(indent=4)
#    if flag == 'protein' or flag == 'gene' or flag == 'qtl':
#        output_file += flag + "_associations.ttl"
    outputWriter = open(output_file, "w")
    # Printing prefixes
    outputWriter.write(base + "\t" + "<" + base_uri + "> .\n")
    outputWriter.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    outputWriter.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    outputWriter.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")    
    outputWriter.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    outputWriter.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    outputWriter.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    outputWriter.write(pr + "\t" + sio_ns + "<" + sio_uri + "> .\n")
#    outputWriter.write(pr + "\t" + ncbi_tax_ns + "<" + ncbi_tax_uri + "> .\n")
    outputWriter.write(pr + "\t" + gr_assoc_ns + "<" + gr_assoc + "> .\n")
    outputWriter.write(pr + "\t" + goa_ns + "<" + goa_uri + "> .\n")
    outputWriter.write(pr + "\t" + up_ns + "<" + uniprot + "> .\n\n")
    #opener = open(files, "r")
    # Slurping all the gaf records into gaf_objs list  
    for infile in files:
        print(infile)
        opener = open(infile, "r")
        gaf_objs = GOA.gafiterator(opener)
        for record in gaf_objs:
            list_records.append(record) #append(record) extend(record)
        opener.close()
    list_records.sort(key=lambda x: x['DB_Object_ID'])
#    pp.pprint(list_records)
    # Accessing individual associations
    for inline in list_records: 
        taxon = ''.join(inline['Taxon_ID'])
        tax_id = taxon.lstrip('taxon:')
#        d = inline['Date']
        date = inline['Date'][:4] + "-" + inline['Date'][4:6] + "-" + inline['Date'][6:]
        if tax_id not in taxon_ids:
            continue
        assoc_line += 1
        ont_term = inline['GO_ID'].replace(":", "_")
        current_obj_id = inline['DB_Object_ID']
        aspect = inline['Aspect']
        go_pattern = re.match(r'^GO', ont_term)
        evidence_code = inline['Evidence']
#        db_ref = inline['DB:Reference']
#        print db_ref

        # Flush
        if previous_obj_id and current_obj_id not in previous_obj_id:
            rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
            outputWriter.write(rdf_buffer)
            rdf_buffer = ''
                
        if current_obj_id not in uniq_obj_id:
            rdf_buffer += up_ns + current_obj_id + "\n"
            rdf_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
            rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + protein_term + " ;\n"
            rdf_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (inline['DB_Object_Symbol']) + " ;\n"
            rdf_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (inline['DB_Object_Name']) + " ;\n"
            for synonym in inline['Synonym']:
                if synonym:
                    rdf_buffer += "\t" + base_vocab_ns + "has_synonym" + "\t" + '"%s"' % (synonym) + " ;\n"
            rdf_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + tax_id + " ;\n"
            uniq_obj_id[current_obj_id] = 1
            previous_obj_id = current_obj_id
                
        # Reification
        if go_pattern:
#            outputWriter.write(goa_ns + current_obj_id + "_" + ont_term + "\n")
            outputWriter.write(goa_ns + current_obj_id + "\n")
        else:
            outputWriter.write(gr_assoc_ns + current_obj_id + "_" + ont_term + "\n")        
#        outputWriter.write(base_ns + "triple_" + current_obj_id + "_" + ont_term + "_" + str(assoc_line) + "\n")
        outputWriter.write("\t" + rdf_ns + "type" + "\t" + rdf_ns +"Statement" + " ;\n")
        outputWriter.write("\t" + rdfs_ns + "subClassOf" + "\t" + sio_ns + sio_term + " ;\n") 
        outputWriter.write("\t" + rdf_ns + "subject" + "\t" + up_ns + current_obj_id + " ;\n")
        outputWriter.write("\t" + rdf_ns + "predicate" + "\t" + base_vocab_ns + ont_aspects[aspect] + " ;\n")
        outputWriter.write("\t" + rdf_ns + "object" + "\t" + obo_ns + ont_term + " ;\n")
        if evidence_code in map_ds:
            for db_ref in inline['DB:Reference']:
                if db_ref in map_ds[evidence_code]:
                    eco_id = map_ds[evidence_code][db_ref].replace(":", "_") 
                    outputWriter.write("\t" + base_vocab_ns + "evidence" + "\t" + obo_ns + eco_id + " ;\n")
                    outputWriter.write("\t" + base_vocab_ns + "evidence_code" + "\t" + '"%s"' % (evidence_code) + " ;\n")
                else:
                    eco_id = map_ds[evidence_code]['Default'].replace(":", "_")
                    outputWriter.write("\t" + base_vocab_ns + "evidence" + "\t" + obo_ns + eco_id + " ;\n")
                    outputWriter.write("\t" + base_vocab_ns + "evidence_code" + "\t" + '"%s"' % (evidence_code) + " ;\n")
        else:
            outputWriter.write("\t" + base_vocab_ns + "evidence_code" + "\t" + '"%s"' % (evidence_code) + " ;\n")
#        outputWriter.write("\t" + base_vocab_ns + "evidence" + "\t" + '"%s"' % (inline['Evidence']) + " ;\n")
        outputWriter.write("\t" + base_vocab_ns + "assigned_by" + "\t" + '"%s"' % (inline['Assigned_By']) + " ;\n")
        outputWriter.write("\t" + base_vocab_ns + "date" + "\t" + '"%s"' % (date) + "^^" + xsd_ns + "date" + " .\n")
                
        # Flushing                
        if current_obj_id == previous_obj_id:
            rdf_buffer += "\t" + base_vocab_ns + ont_aspects[aspect] + "\t" + obo_ns + ont_term + " ;\n"
            if go_pattern:
                rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + goa_ns + current_obj_id + " ;\n"
            else:
                rdf_buffer += "\t" + base_vocab_ns + "has_annotation" + "\t" + gr_assoc_ns + current_obj_id + "_" + ont_term + " ;\n"

        previous_obj_id = current_obj_id

    # Last Flush
    if previous_obj_id:
        rdf_buffer = re.sub(' ;$', ' .', rdf_buffer) 
        outputWriter.write(rdf_buffer)
    outputWriter.close()
    print "Total number of associations: %s\n" % (str(assoc_line))


       
def gafEcoMap(map_file):
    fileHandle = open(map_file, "r")
    map_ds = {}
    lines = fileHandle.readlines()
    for line in lines:
        if not line.startswith('#'):
            line = line.strip()
            evidences = line.split('\t')
            evidence_code = evidences[0]
            if evidence_code not in map_ds:
                map_ds[evidence_code] = {evidences[1]: evidences[2]}
            if evidence_code in map_ds:
                map_ds[evidence_code].update({evidences[1]: evidences[2]}) 
    
    return map_ds



eco_map_file = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/ontology_associations/gaf-eco-mapping.txt'
prot_assoc_test_dir = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/ontology_associations/protein_associations/*.*'
gene_assoc_test_dir = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/ontology_associations/gene_associations/*.*'
qtl_assoc_test_dir = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/test_files/ontology_associations/qtl_associations/*.*'


prot_test_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/Verification_GAF/protein_associations.ttl'
gene_test_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/Verification_GAF/gene_associations.ttl'
qtl_test_output = '/media/elhassouni/donnees/Noeud-plante-projet/workspace/AgroLD/AgroLD_ETL/rdf_ttl/Verification_GAF/qtl_associations.ttl'


prot_gaf_files = glob.glob(prot_assoc_test_dir) #gene_assoc_dir stores file names(with the full path) as a list

#print "************** Protein-ontology associations *************\n"
mapping = gafEcoMap(eco_map_file)
allGafRDF(prot_gaf_files, mapping, prot_test_output, 'protein') #
ProteinGafRDF(prot_gaf_files, mapping, prot_test_output) # allGafRDF(prot_gaf_files, protein_assoc_ttl, 'protein')
print "************** Protein-ontology associations RDF converted *************\n\n"























