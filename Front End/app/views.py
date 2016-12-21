from flask import Flask
from flask import render_template, request
import urllib2, json
import csv
import numpy as np 

app = Flask(__name__, static_url_path='/static')

# Getting the list of fixtures
fixtures_url = "http://localhost:8182/fixtures"
response = urllib2.urlopen(fixtures_url)
json_object = json.load(response)
fixtures = []
for match in json_object['Fixtures']:
	match_string = match['HomeTeam'].replace(" ", "") +"vs"+ match['AwayTeam'].replace(" ", "")
	fixtures.append(match_string)

MATCHES = []
STATS = []
TEAMS = ['Arsenal', 'Bournemouth', 'Burnley', 'Chelsea', 'CrystalPalace', 
'Everton', 'HullCity', 'LeicesterCity', 'Liverpool', 'ManchesterCity', 'ManchesterUnited', 
'Middlesbrough', 'Southampton', 'StokeCity', 'Sunderland', 'SwanseaCity', 'TottenhamHotspur',
'Watford', 'WestBromwichAlbion', 'WestHamUnited']

class Match(object):
	def __init__(self, match):
		self.match = match

	def get_date(self):
		return self.match[0]

	def get_home_team(self):
		return self.match[1]

	def get_away_team(self):
		return self.match[2]

	def get_home_goals(self):
		return self.match[3]

	def get_away_goals(self):
		return self.match[4]

	def get_match_result(self):
		return self.match[5]

class Stats(object):
	def __init__(self, stats):
		self.stats = stats

	def get_team_name(self):
		return self.stats[0]

	def get_home_attacking_strength(self):
		return self.stats[6]

	def get_home_defensive_strength(self):
		return self.stats[7]

	def get_away_attacking_strength(self):
		return self.stats[13]

	def get_away_defensive_strength(self):
		return self.stats[14]


f = open('../Data/Premier League Data.csv', 'rb')
reader = csv.reader(f)
for row in reader:
	MATCHES.append(Match(row))
f.close()

f = open('../Data/Stats.csv','rb')
reader = csv.reader(f)
for row in reader:
	STATS.append(Stats(row))
f.close()

del MATCHES[0] # remove columns titles
del STATS[0] # remove columns titles
# print str(MATCHES[0].get_home_team())

def predict(team1, team2): 
	for team in STATS:
		if team.get_team_name() == team1:
			team1_stats = team
		elif team.get_team_name() == team2:
			team2_stats = team

	home_team_goal_expectancy = float(team1_stats.get_home_attacking_strength()) * float(team2_stats.get_away_defensive_strength()) * 13.75
	away_team_goal_expectancy = float(team2_stats.get_home_attacking_strength()) * float(team1_stats.get_away_defensive_strength()) * 10.15
	

	return np.random.poisson(0,home_team_goal_expectancy)


print predict("ManchesterUnited", "Chelsea")





def get_recent_results(team_name):
	num_of_results = 5
	match_results = []
	for match in MATCHES:
		match_string = ""
		if num_of_results == 0: break
		if match.get_home_team() == team_name:
			if match.get_match_result() == "A":
				match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "L"
				match_results.append(match_string)
				# print str(team_name) + " lost" + str(match.get_date())
			else:
				match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "W"
				match_results.append(match_string)
				# print str(team_name) + " won!" + str(match.get_date())

			# print ""+str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + str(match.get_match_result())+"\n"
			num_of_results = num_of_results - 1

		elif match.get_away_team() == team_name:
			if match.get_match_result() == "H":
				match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "L"
				match_results.append(match_string)
				# print str(team_name) + " lost" + str(match.get_date())
			else:
				match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "W"
				match_results.append(match_string)
				# print str(team_name) + " won!" + str(match.get_date())

			# print ""+str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + str(match.get_match_result())+"\n"
			num_of_results = num_of_results - 1

	return match_results


get_recent_results("ManchesterUnited")

print "##############################"

def get_head_to_head(team_1, team_2):
	num_of_results = 5
	head_to_head_results = []
	for match in MATCHES:
		head_to_head = ""
		if num_of_results == 0: break
		if match.get_home_team() == team_1 and match.get_away_team() == team_2:
			# print match.get_date()
			head_to_head = match.get_date()+": "+str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + str(match.get_match_result())
			head_to_head_results.append(head_to_head)
			num_of_results = num_of_results - 1

		elif match.get_away_team() == team_1 and match.get_home_team() == team_2:
			# print match.get_date()
			head_to_head = match.get_date()+": "+str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + str(match.get_match_result())
			head_to_head_results.append(head_to_head)
			num_of_results = num_of_results - 1

	return head_to_head_results

get_head_to_head("ManchesterUnited", "Chelsea")



###### ROUTES BEGIN HERE ######

@app.route('/', methods=['GET', 'POST'])
def index():
	if request.method=='GET':
		return render_template('home.html', teams=TEAMS)
	elif request.method=='POST':
		text = request.form['text']
		processed_text = text.upper()
		print processed_text
		return render_template('home.html', team_name=processed_text)
	else:
		return render_template('home.html')

	

@app.route('/info', methods=['GET'])
def info():
	if request.method=='GET':
		return render_template('info.html', fixtures=fixtures)
	
	else:
		return("ok")

@app.route("/info/<team>",  methods=['GET', 'POST'])
def info_with_team(team):
	if request.method=='POST':
		# print str(team) + " this is ht eteam"
		# text = request.form['text']
		# processed_text = text.upper()
		# print str(processed_text)
	# return processed_text

		# response = urllib2.urlopen(fixtures_url)
		# # json_raw = response.readlines()
		# json_object = json.load(response)
		# # print json_object['Fixtures'][0]
		# fixtures = []
		# for match in json_object['Fixtures']:
		# 	match_string = match['HomeTeam'].replace(" ", "") +"vs"+ match['AwayTeam'].replace(" ", "")
		# 	fixtures.append(match_string)

		current_fixture = ""
		for match in fixtures:
			print match
			if team in match:
				current_fixture = match
				break
		print current_fixture

		away_team = current_fixture.split('vs')[1]
		print away_team

		return render_template('info.html', fixtures=current_fixture, 
			home_recent_results=get_recent_results(team), 
			away_recent_results=get_recent_results(away_team),
			head_to_head_results=get_head_to_head(team, away_team))
	
	else:
		return "Loading.."

	# if request.method=='GET':
	# 		response = urllib2.urlopen(fixtures_url)
	# 		# json_raw = response.readlines()
	# 		json_object = json.load(response)
	# 		# print json_object['Fixtures'][0]
	# 		fixtures = []
	# 		for match in json_object['Fixtures']:
	# 			match_string = match['HomeTeam'].replace(" ", "") +"vs"+ match['AwayTeam'].replace(" ", "")
	# 			fixtures.append(match_string)

	# 		return render_template('info.html', fixtures=fixtures)
			
		# elif request.method=='POST':
		# 	print "HLKJKSJKJKJKJSKD"
		# 	text = request.form['text']
		# 	processed_text = text.upper()
		# 	print processed_text
		# 	return processed_text
		
	# 	else:
	# 		return("ok")
	
	# return 'You picked '+str(team_name)