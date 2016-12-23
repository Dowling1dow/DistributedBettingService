from flask import Flask
from flask import render_template, request
import urllib2, json, csv, re
import numpy as math 

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

def poisson_dist(x,mean):
    p = math.exp(-mean)
    for i in xrange(x):
        p *= mean
        p /= i + 1
    return p

def predict(team1, team2):
	for team in STATS:
		if team.get_team_name() in team1 or team.get_team_name() == team1:
			print "MATCH 1"
			team1_stats = team
		if team.get_team_name() in team2 or team.get_team_name() == team2:
			print "MATCH 2"
			team2_stats = team

	#Home team attacking strength * Away Team Defensive Strength * Average Goals Home 1.64
	home_team_goal_expectancy = float(team1_stats.get_home_attacking_strength()) * float(team2_stats.get_away_defensive_strength())*1.64
	# Away team attacking strength * Home Team Defensive Strength * Average Goals Away 1.19
	away_team_goal_expectancy = float(team2_stats.get_away_attacking_strength()) * float(team1_stats.get_home_defensive_strength())*1.19
	score={}
	for i in range(0,8):
		for j in range (0,8):
			score[i,j]=(poisson_dist(i,home_team_goal_expectancy)*poisson_dist(j,away_team_goal_expectancy))*100
			score[i,j]=round(score[i,j],2)

	home_win=0;
	away_win=0;
	draw=0;
	for i in range(0,8):
		for j in range(0,8):
			if(i==j):
				draw=draw+score[i,j]
			elif (i<j):
				away_win=away_win+score[i,j]
			else:
				home_win=home_win+score[i,j]

	probabilities = []
	probabilities.append(str(home_win)+"%")
	probabilities.append(str(draw)+"%")
	probabilities.append(str(away_win)+"%")

	return probabilities


def get_recent_results(team_name):
	num_of_results = 5
	match_results = []
	for match in MATCHES:
		match_string = ""
		if num_of_results == 0: break
		if match.get_home_team() == team_name:
			if match.get_match_result() == "A":
				# match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "L"
				match_string = str(match.get_home_team())+' vs '+str(match.get_away_team())+"</br>"+str(match.get_home_goals()+" - "+match.get_away_goals())
				match_results.append(match_string)
				match_results.append("#ff5050")
			else:
				# match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "W"
				match_string = str(match.get_home_team())+' vs '+str(match.get_away_team())+"</br>"+str(match.get_home_goals()+" - "+match.get_away_goals())
				match_results.append(match_string)
				match_results.append("#66cc66")

			num_of_results = num_of_results - 1

		elif match.get_away_team() == team_name:
			if match.get_match_result() == "H":
				# match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "L"
				match_string = str(match.get_home_team())+' vs '+str(match.get_away_team())+"</br>"+str(match.get_home_goals()+" - "+match.get_away_goals())
				match_results.append(match_string)
				match_results.append("#ff5050") 
			else:
				# match_string = str(match.get_home_team())+" vs "+str(match.get_away_team())+ " Result: " + "W"
				match_string = str(match.get_home_team())+' vs '+str(match.get_away_team())+"</br>"+str(match.get_home_goals()+" - "+match.get_away_goals())
				match_results.append(match_string)
				match_results.append("#66cc66")

			num_of_results = num_of_results - 1

	return match_results


def get_head_to_head(team_1, team_2):
	num_of_results = 5
	head_to_head_results = []
	for match in MATCHES:
		head_to_head = ""
		if num_of_results == 0: break
		if match.get_home_team() == team_1 and match.get_away_team() == team_2:
			# print match.get_date()
			head_to_head = match.get_date()+"<br/> "+str(match.get_home_team())+" vs "+str(match.get_away_team())+ "<br/>" + str(match.get_home_goals()+" - "+match.get_away_goals())
			head_to_head_results.append(head_to_head)
			num_of_results = num_of_results - 1

		elif match.get_away_team() == team_1 and match.get_home_team() == team_2:
			# print match.get_date()
			head_to_head = match.get_date()+"<br/> "+str(match.get_home_team())+" vs "+str(match.get_away_team())+ "<br/>" + str(match.get_home_goals()+" - "+match.get_away_goals())
			head_to_head_results.append(head_to_head)
			num_of_results = num_of_results - 1

	return head_to_head_results


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

	current_fixture = ""
	for match in fixtures:
		print match
		if team in match:
			current_fixture = match
			break
	print "\nCurrentF: "+str(current_fixture)
	if current_fixture.split('vs')[0] == team:
		home_team = team
		away_team = current_fixture.split('vs')[1]
	else:
		home_team = current_fixture.split('vs')[0]
		away_team = team

	print "HomeTeam: "+str(home_team)
	print "AwayTeam: "+str(away_team)

	# Team names with spaces
	home_team_ws = re.sub(r"(?<=\w)([A-Z])", r" \1", home_team)
	away_team_ws = re.sub(r"(?<=\w)([A-Z])", r" \1", away_team)

	# Getting SkyBet odds
	sky_response = urllib2.urlopen("http://localhost:8182/skybet/footballMatches/SB"+home_team+"vs"+away_team)
	sb_json_object = json.load(sky_response)

	# Getting WilliamHill odds
	wh_response = urllib2.urlopen("http://localhost:8182/williamhill/footballMatches/WH"+home_team+"vs"+away_team)
	wh_json_object = json.load(wh_response)

	# Getting BetVictor odds
	bv_response = urllib2.urlopen("http://localhost:8182/betvictor/footballMatches/BV"+home_team+"vs"+away_team)
	bv_json_object = json.load(bv_response)


	return render_template('info.html', 
		home_team=current_fixture.split('vs')[0],
		away_team=current_fixture.split('vs')[1],
		
		home_team_ws=home_team_ws,
		away_team_ws=away_team_ws,
		predictions=predict(home_team, away_team),

		sb_home_win=sb_json_object['HomeTeamWin'],
		sb_away_win=sb_json_object['AwayTeamWin'],
		sb_draw=sb_json_object['Draw'],

		wh_home_win=wh_json_object['HomeTeamWin'],
		wh_away_win=wh_json_object['AwayTeamWin'],
		wh_draw=wh_json_object['Draw'],

		bv_home_win=bv_json_object['HomeTeamWin'],
		bv_away_win=bv_json_object['AwayTeamWin'],
		bv_draw=bv_json_object['Draw'],

		home_recent_results=get_recent_results(home_team), 
		away_recent_results=get_recent_results(away_team),
		head_to_head_results=get_head_to_head(home_team, away_team))
	



