import re
with open("data.txt","r") as file:
    data = file.read()


lines = data.splitlines()
with open("data.csv","w") as outFile:
    outFile.write("Start Date\tEnd Date\tVested Balance\tBeginning Balance\tEmployer Contributions\tEnding Balance\tExchange In\tExchange Out\tFees\tInvestment Gain/Loss\tYour Contributions\tYour Personal Rate of Return This Period\tYour Rollover Contributions\n")
    for line in lines:
        line = re.sub(r'[$]','',line)
        line = re.sub(r'[%]',' ',line)
        startDate = re.search(r'.*Statement Period: ([0-9/]*) to .*',line).group(1)
        endDate = re.search(r'.*Statement Period: [0-9/]* to ([0-9/]*) .*',line).group(1)
        items = ["Vested Balance", "Beginning Balance", "Employer Contributions", "Ending Balance", "Exchange In", "Exchange Out", "Fees", "Investment Gain/Loss", "Your Contributions", "Your Personal Rate of Return This Period", "Your Rollover Contributions"]
        outFile.write(startDate+"\t"+endDate)
        for item in items:
            regex = re.compile(".* "+item+" ([0-9,.-]*) .*")
            result = re.search(regex,line)
            if result == None:
                itemValue = ""
            else:
                itemValue = re.search(regex,line).group(1)
            outFile.write("\t"+itemValue)
        outFile.write("\n")

