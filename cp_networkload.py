__author__ = 'Mgamerz'

from tkinter import *
from tkinter import Canvas
from tkinter import ttk
import requests
from requests.auth import HTTPDigestAuth
import getpass

#Uncomment this to make the username/password/ip prompt appear in the terminal.
#Make a file called local_credentials with your username password routerip in it, all on separate lines, in that order.
read_config = True

class ServoDrive(object):

    def get_userpass(self):
        username = input()
        password = getpass.getpass()
        return username, password

    def get_routerip(self):
        print('Router IP:', end=' ')
        return input()

    def read_login(self):
        with open('local_credentials') as f:
            user = f.readline().rstrip('\n')
            passw = f.readline().rstrip('\n')
            self.userpass = (user, passw)
            self.routerip = f.readline().rstrip('\n')

            print('{} {} {}'.format(user, passw, self.routerip))

    def __init__(self):
        if read_config:
            self.read_login()
        else:
            self.userpass = self.get_userpass()
            self.routerip = self.get_routerip()
        self.auth = HTTPDigestAuth(self.userpass[0], self.userpass[1])

    def getLoad(self):
        subtree = '/api/status/system/cpu/'
        try:
            cpudata = requests.get('http://{}{}'.format(self.routerip, subtree), auth=self.auth)
            data = cpudata.json()['data']
            userload = round(data['user']*100)
            systemload = round(data['system']*100)
            return (userload, systemload)

        except Exception as e:
            print(e)

class CPULoad(ttk.Frame):
    def __init__(self, *args, **kwargs):
        ttk.Frame.__init__(self, *args, **kwargs)
        self.servo = ServoDrive()
        self.canvas = Canvas(self, background="black")
        self.canvas.pack(side="bottom", fill="both", expand=True)

        # create line for load
        self.user_line = self.canvas.create_line(0, 100, 0, 100, fill="green")
        self.system_line = self.canvas.create_line(0, 100, 0, 100, fill='orange')
        self.total_line = self.canvas.create_line(0, 100, 0, 100, fill='red')

        # start the update process
        self.update_graph()

    def update_graph(self):
        load = self.servo.getLoad()
        self.add_point(self.user_line, load[0])
        self.add_point(self.system_line, load[1])
        total_time = load[0] + load[1]
        if total_time > 100:
            total_time = 100
        self.add_point(self.total_line, total_time)
        self.canvas.xview_moveto(1.0)
        self.after(150, self.update_graph)

    def add_point(self, line, y):
        coords = self.canvas.coords(line)
        y = 150 - y*1.5
        x = coords[-2] + 10
        coords.append(x)
        coords.append(y*2)
        coords = coords[-80:] # keep # of points to a manageable size
        self.canvas.coords(line, *coords)
        self.canvas.configure(scrollregion=self.canvas.bbox("all"))

if __name__ == "__main__":
    root = Tk()
    root.title('Router CPU Monitor')
    root.minsize(400, 240)
    cpuframe = ttk.Frame(root)
    cpuframe.pack(side='right', expand=True, fill='both')
    CPULoad(cpuframe).pack(fill='both', expand=True)
    ttk.Label(cpuframe, text='Time').pack(side='bottom')

    loadframe = ttk.Frame(root)
    loadframe.pack(side='left', expand=False, fill='none')
    ttk.Label(loadframe, text='Load').pack(side='top', fill=Y)
    ttk.Label(loadframe, text='User', foreground='green').pack(side='bottom', fill=Y)
    ttk.Label(loadframe, text='Total', foreground='red').pack(side='bottom', fill=Y)
    ttk.Label(loadframe, text='System', foreground='orange').pack(side='bottom', fill=Y)


    root.resizable(0,0)
    root.config(background='grey')
    root.mainloop()