__author__ = 'Mgamerz'

import tkinter as tk
import random
import requests
from requests.auth import HTTPDigestAuth
import getpass

class ServoDrive(object):
    # simulate values

    def get_userpass(self):
        print('Username:', end=' ')
        username = input()
        password = getpass.getpass()
        return (username, password)

    def get_routerip(self):
        print('Router IP:', end=' ')
        return input()

    def read_login(self):
        with open('local_credentials') as f:
            user = f.readline()
            passw = f.readline()
            self.userpass = (user, passw)
            self.routerip = f.readline()

    def __init__(self):
        #self.userpass = self.get_userpass()
        self.read_login()
        self.auth = HTTPDigestAuth(self.userpass[0], self.userpass[1])
        #self.routerip = self.get_routerip()


    def getLoad(self):
        subtree = '/api/status/system/cpu'
        cpudata = requests.get('http://{}{}'.format(self.routerip, subtree), auth=self.auth)
        '''if cpudata.status_code == 200:
            print(cpudata.json())
        else:
            print(cpudata.status_code)'''

#{"data": {"system": 0.0024232633279483038, "user": 0.045234248788368332, "nice": 0.0}, "success": true}'

        return random.randint(0, 50)

class Example(tk.Frame):
    def __init__(self, *args, **kwargs):
        tk.Frame.__init__(self, *args, **kwargs)
        self.servo = ServoDrive()
        self.canvas = tk.Canvas(self, background="black")
        self.canvas.pack(side="bottom", fill="both", expand=True)

        # create line for load
        self.load_line = self.canvas.create_line(0, 0, 0, 0, fill="red")

        # start the update process
        self.update_graph()

    def update_graph(self):
        v = self.servo.getLoad()
        self.add_point(self.load_line, v)
        self.canvas.xview_moveto(1.0)
        self.after(1000, self.update_graph)

    def add_point(self, line, y):
        coords = self.canvas.coords(line)
        x = coords[-2] + 20
        coords.append(x)
        coords.append(y)
        coords = coords[-20:] # keep # of points to a manageable size
        self.canvas.coords(line, *coords)
        self.canvas.configure(scrollregion=self.canvas.bbox("all"))

if __name__ == "__main__":
    root = tk.Tk()
    Example(root).pack(side="bottom", fill="both", expand=True)
    root.mainloop()