import sys
import json
import pprint
import codecs 

def lines(fp):
    print str(len(fp.readlines()))
    
tweet_input = '/home/venkatesan/coursera/data_science/datasci_course_materials/assignment1/problem_1_submission.txt' #output.txt problem_1_submission.txt
sentiment_input = '/home/venkatesan/coursera/data_science/datasci_course_materials/assignment1/AFINN-111.txt'

pp = pprint.PrettyPrinter(indent=4)


def sentiment(infile):
    scores = dict()
    sentiment_file = open(infile, "r")
    
    for line in sentiment_file:
        term, score = line.split("\t")
        scores[term] = int(score)
        
    sentiment_file.close()
    return scores
    
sentiment_scores = sentiment(sentiment_input)

#infile = open(tweet_input, "r")
infile = codecs.open(tweet_input,'r', encoding='utf-8')#.read()
lines = infile.readlines()
data_struc = dict()#dict() json.loads(infile)

#with codecs.open(tweet_input) as infile: #, 'rU', 'utf-8'
#    for line in infile:
#        data_struc.update(json.loads(line, encoding='utf-8'))

for line in lines:
#    try:
    data_struc.update(json.loads(line)) #, encoding='utf-8'
#    except:
#        print "Error in line:" + line #continue
            

#for line in lines:
#    data_struc.update(json.loads(line, encoding='utf-8'))
#print len(data_struc)
#pp.pprint(data_struc)
score = 0
tweets = list()
for data in data_struc:
#    for sentiment in sentiment_scores:
#        sentiment.decode()
    if data == "text":
        tweets.append(data_struc[data].encode('utf-8'))
#        tweet = data_struc[data].encode('utf-8')
#pp.pprint(tweets)            
#            if sentiment in tweet: #data_struc[data]:
#                score += sentiment_scores[sentiment]
#                print sentiment + "\t" + str(sentiment_scores[sentiment])#str(sentiment_scores[sentiment]) str(score)
#            else:
#                print score
for sentiment in sentiment_scores:
    for tweet in tweets:
        if sentiment in tweet:
            score += sentiment_scores[sentiment]
         


print score
#            if sentiment not in tweet:
                
#            print data_struc[data]
            
#            data_struc[data].decode('utf-8')
#            if sentiment in data_struc[data]:
#                print sentiment + "\t" + str(sentiment_scores[sentiment]) 
#for data in data_struc:
#    if data == 'text':
#        print data_struc[data]
#    pp.pprint(data)
infile.close()
