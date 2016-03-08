'''
Created on Mar 23, 2015

@author: venkatesan
'''
#import sys
#sys.path.append("/home/venkatesan/Downloads/biopython-1.65/Bio")

from Bio import SwissProt

from riceKB.globalVars import *
import pprint
import re
import os
from _collections import defaultdict

def upToRDF(up_files, rdf_out_dir): #, output_file
    
    rdf_file = "uniport.plants.ttl"
    output_file = os.path.join(rdf_out_dir, rdf_file)
    output_writer = open(output_file, "w")
    rdf_buffer = ''
    prot_counter = 0
    pp = pprint.PrettyPrinter(indent=4)
    up_base_uri = "http://purl.uniprot.org/"
#    up_base_ns = "uniprot_base:"
    
    print "************* Converting Uniprot data to RDF ***************\n"
    
    output_writer.write(base + "\t" + "<" + base_uri + "> .\n")
    output_writer.write(pr + "\t" + rdf_ns + "<" + rdf + "> .\n")
    output_writer.write(pr + "\t" + rdfs_ns + "<" + rdfs + "> .\n")
    output_writer.write(pr + "\t" + owl_ns + "<" + owl + "> .\n")    
#    output_writer.write(pr + "\t" + xsd_ns + "<" + xsd + "> .\n")
    output_writer.write(pr + "\t" + base_vocab_ns + "<" + base_vocab_uri + "> .\n")
    output_writer.write(pr + "\t" + obo_ns + "<" + obo_uri + "> .\n")
    output_writer.write(pr + "\t" + sio_ns + "<" + sio_uri + "> .\n")
#    output_writer.write(pr + "\t" + ncbi_tax_ns + "<" + ncbi_tax_uri + "> .\n")
#    output_writer.write(pr + "\t" + up_base_ns + "<" + up_base_uri + "> .\n")
    output_writer.write(pr + "\t" + up_ns + "<" + uniprot + "> .\n\n")
    
    for upfile in up_files:
        file_handle = open(upfile, "r")
        up_records = SwissProt.parse(file_handle)
#        xrefs = defaultdict(list)
#        xref_ids = list()             
        for record in up_records:
            xrefs = defaultdict(list)
            rdf_buffer = ''
            for taxID in record.taxonomy_id:
                if taxID in taxon_ids:
#                    rdf_buffer = ''                   
                    # Accession
                    if len(record.accessions) > 1:
                        prim_accession = record.accessions.pop(0)
                        prot_counter += 1 
                        rdf_buffer += up_ns + prim_accession + "\n" #output_writer.write(up_ns + prim_accession + "\n")
                        rdf_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n" #output_writer.write("\t" + rdf_ns + "type" + "\t" + base_vocab_ns + "Protein" + " ;\n")
                        rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + protein_term + " ;\n" #output_writer.write("\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + protein_term + " ;\n")
                        for altID in record.accessions:
                            rdf_buffer += "\t" + base_vocab_ns + "has_alternative_id" + "\t" + up_ns + altID + " ;\n" #output_writer.write("\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + protein_term + " ;\n")
                    else:
                        prim_accession = record.accessions[0]
                        prot_counter += 1
                        rdf_buffer += up_ns + prim_accession + "\n"
                        rdf_buffer += "\t" + rdf_ns + "type" + "\t" + owl_ns + "Class" + " ;\n"
                        rdf_buffer += "\t" + rdfs_ns + "subClassOf" + "\t" + obo_ns + protein_term + " ;\n"
                         
                    # Label
                    print record.entry_name
                    rdf_buffer += "\t" + rdfs_ns + "label" + "\t" + '"%s"' % (record.entry_name) + " ;\n"
                    
                    # Description
                    if record.description:
                        descriptions = record.description.split(';')
                        description = descriptions[0][14:]#.lstrip('RecName: Full=')
                        rdf_buffer += "\t" + base_vocab_ns + "description" + "\t" + '"%s"' % (description) + " ;\n"
#                    print description

                    #  Gene Name
#                    print record.gene_name              
                    if record.gene_name:
                        raw_strings = record.gene_name.split(';')
#                        print raw_strings
                        string_name = raw_strings[0]
                        gene_names = string_name.split('=')#record.gene_name.lstrip('Name=')
                        search_pattern = re.search("\s{", gene_names[1])
                        if search_pattern:
                            names = re.split("\s{", gene_names[1])
                            symbol = names[0]                    
                            rdf_buffer += "\t" + base_vocab_ns + "has_symbol" + "\t" + '"%s"' % (symbol) + " ;\n" #gene_names[0].lstrip('Name=')
                        else:
                            symbol = gene_names[1]
                            rdf_buffer += "\t" + base_vocab_ns + "has_symbol" + "\t" + '"%s"' % (symbol) + " ;\n"
#                    print symbol #gene_names[0].lstrip('Name=')
#                    for name in gene_names:
#                        name = name.lstrip(' ')
#                        print name

                    # Taxon
                    rdf_buffer += "\t" + base_vocab_ns + "taxon" + "\t" + obo_ns + "NCBITaxon_" + taxID + " ;\n"
#                   taxID
                   
                    # Comments
                    if record.comments:
                        raw_comment = ''.join(record.comments)
                        comment = raw_comment.replace('"', '')
                        rdf_buffer += "\t" + base_vocab_ns + "comment" + "\t" + '"%s"' % (comment) + " ;\n"
#                   print (comment)
                    
                    # Keywords
#                    print record.keywords
                    if record.keywords:
                        for keyword in record.keywords:
#                            print keyword
                            rdf_buffer += "\t" + base_vocab_ns + "classified_with" + "\t" + '"%s"' % (keyword) + " ;\n"
                    # Cross References
#                    pp.pprint(record.cross_references[0])
                    
                    for dbs in record.cross_references:
                        dbname = dbs[0]
                        ids = dbs[1]
                        xrefs[dbname].append(ids)
                    
                    for key in xrefs:
                        if key != "GO":
                            db_namespace = key.lower()
                            for dbid in xrefs[key]:
                                rdf_buffer += "\t" + base_vocab_ns + "has_dbxref" + "\t" + "<" + up_base_uri + db_namespace + "/" + dbid + ">" + " ;\n"
                    
                    # Corss references using blank node
#                    for key in xrefs:
#                        rdf_buffer += "\t" + base_vocab_ns + "has_dbxref" + "\t" + "[" + "\n"
#                        rdf_buffer += "\t" + "\t" +  base_vocab_ns + "dbname" + "\t" + '"%s"' % (key) + " ;\n" #"[" +
#                        for dbid in xrefs[key]:
#                            rdf_buffer += "\t" + "\t" + base_vocab_ns + "id" + "\t" + '"%s"' % (dbid) + " ;\n"
#                        rdf_buffer = re.sub(' ;$', '', rdf_buffer)    
#                        rdf_buffer += "\t" + "\t" + "]" + " ;\n" 

                    rdf_buffer = re.sub(' ;$', ' .', rdf_buffer)
                    output_writer.write(rdf_buffer)
        file_handle.close()
    output_writer.close()
    print "Number of Proteins: %s\n" % (str(prot_counter))
    print "*************** UniProt RDF conversion completed ************\n"
                
#                   pp.pprint(record.cross_references) #taxonomy_id cross_references comments description keywords gene_name molecule_type
       
        