.\" ul.tmac
.\"
.\" Copyright (C) 2003, 2004, 2009, 2010
.\"   Free Software Foundation, Inc.
.\"
.\"   written by Werner LEMBERG <wl@gnu.org>
.\"
.\" ul.tmac is free software: you can redistribute it and/or modify
.\" it under the terms of the GNU General Public License as published
.\" by the Free Software Foundation, either version 3 of the License,
.\" or (at your option) any later version.
.\"
.\" ul.tmac is distributed in the hope that it will be useful, but
.\" WITHOUT ANY WARRANTY; without even the implied warranty of
.\" MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
.\" GNU General Public License for more details.
.\"
.\" You should have received a copy of the GNU General Public License
.\" along with GNU Emacs.  If not, see <http://www.gnu.org/licenses/>.
.\"
.\"
.\" History:
.\"
.\"   Version 1.0  2003-Dec-18
.\"     First release.
.\"
.\"   Version 1.1  2004-Jan-14
.\"     Improve documentation.
.\"
.\"   Version 1.2  2004-Jun-09
.\"     Add GPL.
.\"
.\"   Version 1.3  2009-Jul-31
.\"     Make underlining work with .ce and .rj requests.
.\"
.\"   Version 1.4  2010-Aug-26
.\"     Provide `.Underline1' macro
.\"     Fix underlining if it starts a line due to paragraph filling.
.\"
.\"
.\" This file defines two macros `.Underline' and `.Underline1', which
.\" underline its arguments continuously.  They are completely
.\" transparent to justifying, this is, the text to be underlined (and
.\" the text surrounding it) is typeset identically to normal text,
.\" without any distortion in filling.
.\"
.\" Contrary to `.Underline' where all arguments are underlined,
.\" `.Underline1' only takes two arguments, where the second is
.\" appended to the first without being underlined.  This is useful
.\" for trailing punctuation, for example.
.\"
.\" Note that you can use both macros in diversions; they also work
.\" across page breaks and are robust against vertical position traps.
.\" Actually, they should work with all macro packages which don't use
.\" the diversion trap (none of the packages distributed with groff
.\" use it).
.\"
.\" With the -me package it might happen that you get a warning (and
.\" no underlining) if the text to be underlined is in the last line
.\" of the document:
.\"
.\"   Line  -- Unclosed block, footnote, or other diversion (ul-div)
.\"
.\" Simply add a final `.br' to your document to get correct
.\" behaviour.
.
.
.\" The vertical position and thickness of the underline segments can
.\" be controlled with the following two strings.
.
.ds Underline-thickness (\En[.ps]s / 15u)
.ds Underline-offset (\En[.ps]s / 5u)
.
.
.if n \{\
.  de Underline
.    cu
\\$*
.  .
.
.  de Underline1
.    cu
\\$1\c
\\$2
.  .
.
.  nx
.\}
.
.
.eo
.
.
.de \"
..
.
.
.de Underline
.  ul-do "\$*"
..
.
.
.de Underline1
.  ul-do "\$1" "\$2"
..
.
.
.de ul-do
.  \" The .ce and .rj requests must be handled specially since they
.  \" don't set \n[.k].
.  if \n[.ce] \{\
.    ul-simple "\$1" "\$2"
.    return
.  \}
.
.  if \n[.rj] \{\
.    ul-simple "\$1" "\$2"
.    return
.  \}
.
.  \" If we aren't in our `ul-div' diversion, start it now, reset the
.  \" .Underline and diversion line counters, and set the diversion
.  \" trap.  Otherwise, just increase the .Underline counter.
.  \"
.  ie '\n[.z]'ul-div' \
.    nr ul-count +1
.  el \{\
.    \" Note that normally a diversion starts with a new line.
.    \" However, to ensure proper undistorted filling, we
.    \" intentionally don't insert a .br request.
.    di ul-div
.    dt 1u ul-do1
.    nr ul-count 0
.    nr ul-line 0
.  \}
.
.  nr ul-active 1
.
.  \" Embed code to set various registers before and after the text.
.  \" This code is evaluated when the diversion is reread the first
.  \" time.
.  \"
.  \" Saving \n[.k] as horizontal positions is sufficient since the
.  \" indentation is set to zero while rereading.  On the other hand,
.  \" we need the (normal) indentation and line length values while
.  \" filling the diversion, to get the start and end positions of
.  \" normal lines in case an underline segment is broken across
.  \" lines.
.  \"
.  \" Saving \n[.d] is necessary since it can happen that formatting
.  \" of a paragraph moves the beginning of the first underline
.  \" segment to the start of the next line.  Later on we check
.  \" whether this value is non-zero, indicating that during the
.  \" rereading of the diversion (which doesn't change the formatting
.  \" any more) the beginning is no longer positioned in the first
.  \" line.
\?\
\R'ul-.d-\n[ul-count] \En[.d]'\
\R'ul-.i-\n[ul-count] \n[.i]'\
\R'ul-.l-\n[ul-count] \n[.l]'\
\R'ul-.ll-\n[ul-count] \n[.ll]'\
\R'ul-start-pos-\n[ul-count] \En[.k]'\
\?\
\$1\
\?\
\R'ul-end-pos-\n[ul-count] \En[.k]'\
\?\
\$2
.
.  nr ul-active 0
..
.
.
.de ul-simple
\Z'\
\D't \*[Underline-thickness]''\c
\Z'\$1'\
\v'\*[Underline-offset]'\
\D'l \w'\$1'u 0'\
\v'-\*[Underline-offset]'\
\$2
..
.
.
.de ul-do1
.  \" Switch to another environment to preserve the partially filled
.  \" line.  In particular, this preserves the markers inserted by a
.  \" previous .Underline macro.
.  ev ul-env
.  evc 0
.
.  \" Emit vertical space possibly truncated by the diversion trap.
.  if \n[.trunc] \
.    sp \n[.trunc]u
.
.  \" Save the .Underline and `active' counters as array elements,
.  \" indexed by the diversion line counter.
.  nr ul-count-\n[ul-line] \n[ul-count]
.  nr ul-active-\n[ul-line] \n[ul-active]
.
.  \" End diversion if there isn't a broken underline segment.
.  \" Otherwise, move trap down to handle next line.
.  ie !\n[ul-active] \{\
.    dt
.    di
.
.    \" Prepare diversion handling.
.    in 0
.    nf
.
.    \" Copy the diversion to set the embedded number registers.
.    di ul-div1
.    ul-div
.    di
.    rn ul-div1 ul-div
.
.    \" Process diversion again.  The trick is to insert underline
.    \" segments *before* the corresponding text line is emitted.
.    nr ul-max-line \n[ul-line]
.    nr ul-count 0
.    nr ul-line 0
.
.    di ul-div1
.    \" Call .ul-do2 manually the first time.
.    ul-do2
.    ul-div
.    di
.
.    \" Finally emit underlined text.
.    ul-div1
.  \}
.  el \
.    dt (\n[.d]u + 1u) ul-do1
.
.  nr ul-line +1
.
.  \" Restore previous environment.
.  ev
..
.
.
.de ul-do2
.  if (\n[ul-line] <= \n[ul-max-line]) \{\
.    nr ul-last \n[ul-count]
.
.    \" Get saved counters from the array.
.    nr ul-count \n[ul-count-\n[ul-line]]
.    nr ul-active \n[ul-active-\n[ul-line]]
.
.    \" Set line thickness.
\Z'\
\D't \*[Underline-thickness]''\c
.
.    \" A typical diversion to be handled looks like this:
.    \"
.    \"    xxxx -0--- xxxx -1----- xxxxxxxx -2--
.    \"    -2------- xxxxxx xxxxx -3----- xxxxxx
.
.    while (\n[ul-last] < \n[ul-count]) \{\
\Z'\
\h'\n[ul-start-pos-\n[ul-last]]u'\
\v'\*[Underline-offset]'\
\D'l (\n[ul-end-pos-\n[ul-last]]u - \n[ul-start-pos-\n[ul-last]]u) 0'\
\v'-\*[Underline-offset]''\c
.      nr ul-last +1
.    \}
.
.    ie \n[ul-active] \{\
.      \" An underline segment broken across lines.
.      ie !\n[ul-.d-\n[ul-count]] \
\Z'\
\h'\n[ul-start-pos-\n[ul-count]]u'\
\v'\*[Underline-offset]'\
\D'l (\n[ul-.ll-\n[ul-count]]u - \n[ul-start-pos-\n[ul-count]]u) 0'\
\v'-\*[Underline-offset]''\c
.      el \
.        nr ul-.d-\n[ul-count] 0
.
.      \" Set start position to the indentation value of next line.
.      nr ul-start-pos-\n[ul-count] \n[ul-.i-\n[ul-count]]
.      \" Update length of next line.
.      nr ul-.ll-\n[ul-count] \n[ul-.l-\n[ul-count]]
.    \}
.    el \{\
.      \" The last underline segment in the diversion.
\Z'\
\h'\n[ul-start-pos-\n[ul-count]]u'\
\v'\*[Underline-offset]'\
\D'l (\n[ul-end-pos-\n[ul-count]]u - \n[ul-start-pos-\n[ul-count]]u) 0'\
\v'\*[Underline-offset]''\c
.    \}
.  \}
.
.  \" Move trap down to handle next line or remove trap.
.  ie \n[ul-active] \
.    dt (\n[.d]u + 1u) ul-do2
.  el \
.    dt
.
.  nr ul-line +1
..
.
.ec
.
.\" EOF
.po .5i
.ll 7.5i
.lt 7.5i
.de TP
'SP .5i
..
.PF "'Page \\\\nP of 3'''"
.ce 1
.B "Offer Letter \- Traveling Merchandiser"
.SP
This document is to outline employee's rate of pay and should be read in conjunction with the Employee Handbook. Employment is
subject to the company's policies, procedures, and handbooks adopted, all of which can be revised at the company's discretion.
The Employee Handbook and Company Policies should be accessed and acknowledged online at workforcenow.adp.com at the time
of hire. Pay stubs can also be accessed online at workforcenow.adp.com. Detailed instructions of the onboarding process can be
found at www.wtgmerch.com/finalinfo (https://www.wtgmerch.com/finalinfo).
This offer and your employment with Way To Go Merchandising and Staffing is contingent upon successful completion of a background check.
.SP
Way To Go Merchandising and Staffing is an At-Will employer, the Company or Employee may terminate the employment
relationship at any time, for any reason, with or without cause or notice.
.SP
.B "Hourly Rate Amount:" 
$PAY-RATE
.SP
.B "Weekly Bonus:"
$3.00 / hour (details below)
.SP
.B 
Travel Time / Training Hourly Pay: 
.Underline Minimum Wage $7.25 per hour
.R
.SP
Hours worked in the store directly related to assigned project. Employees work schedule and hours worked will vary depending on
the needs of the project and Employer. Schedule may include 1 st , 2 nd or 3 rd shift or a combination thereof and may include
overtime or partial shifts. Ensure timesheets are completed daily and submitted to the Home Office. Employee must sign timesheet
each day. Employee work week runs from Saturday through Friday each week and payroll is paid the following Friday.
.SP
.B "Hourly Pay" 
\- Represents hours worked times hourly rate up to 40 hours. Overtime rate of 1.5 times hourly rate paid for hours
worked over 40. Hourly pay is subject to payroll deductions (i.e. federal and state income taxes, social security, medicare, etc) and
could be subject to wage garnishments. Compensation may be modified at any time at the company's discretion. When hired,
employees default to single with zero withholding allowances for purposes of the Federal W-4 and state equivalent. It is the
employee's responsibility to log on to workforcenow.adp.com and update if necessary.
.SP
.B "Hotels"
\- Company will provide hotel accommodations during travel on projects lasting greater than 3 weeks.
.SP
.BL
.LI
Employee's Per Diem Rate will be $20 / day worked when Hotels are paid by Way To Go.
.LI
Rooms booked may or may not allow pets and/or smoking in the room. Employee must abide by hotel guidelines and policies.
.LI
Hotel location is subject to change based on availability.
.LI
Associates are required to have a travel partner or roommate. Way To Go will not pay for a hotel for only one Associate unless
prior written approval is obtained from the CFO.
.LI
Any charges for damages, unapproved pets, smoking or any other related charges will be the responsibility of the Employee
and will be deducted from the Employee's next paycheck. If Employee does not check out once they finish working, any
additional days charged to the Company will be deducted from the Employee's next paycheck.
.LI
Employees are expected to work 5 days per week. Staying in a hotel and not working, showing up late, leaving early or
otherwise not working the full scheduled shift is considered a PERSONAL hotel stay. A portion of the weekly hotel cost will be
deducted from Employee's paycheck if they do not work the full assigned shift for that day, calculated as one-fifth (1/5) of the
employee's share of the weekly cost of the hotel room per day missed. For example, if the weekly hotel cost is $600 and
employee misses 1 day of work, then $60 will be deducted from their paychecks a personal hotel night ($600 divided by 5
days divided by 2 employees sharing the room). If the weekly hotel cost is $800 and employee shows up 30 minutes late to
work, then $80 will be deducted from their paycheck as a personal night ($800 divided by
5 days divided by 2 employees sharing the room).
.LI
Most hotels require someone 21 years or older to be staying in the room.
.LE
.SP
.B "Per Diem"
\- Company will pay daily Per Diem on projects lasting 3 weeks or less.
.SP
.B
Associates \- $60 / day worked up to 5 days per week
.SP
Supervisors \- $100 / day worked up to 5 days per week
.R
.SP
Employees on projects lasting more than 3 weeks who have worked greater than 1,000 hours on Projects
(excluding drive time and training time) may request to receive Per Diem and not get their hotel paid for by Way To Go.
This request must be made directly with WTG's Recruiting Department at (918) 820-3060 at least 2 weeks PRIOR to the start of a Project.
This election will be made on a project-by-project basis and cannot be changed during the project unless prior written approval is obtained from the CFO.
.SP
**Per Diem allowance is for ordinary and necessary business travel expenses, including automobile/mileage, lodging, tips and
business meals. The Per Diem allowance is only paid when employee is traveling away from home, in excess of a normal commute. If
employee lives within 1 hour of job site based on Google Maps travel time, employee will not receive Per Diem. Employees are
required to return to the Company any portion of the Per Diem allowance not used for ordinary and necessary business travel
expenses. Any travel expenses incurred above and beyond the Per Diem allowance will NOT be reimbursed to the employee unless
prior written approval is obtained from the CFO. Per Diem may be modified at any time at the company's discretion. Per Diem is
a daily allowance paid the day after an employee works a minimum of 8 hours in a store or the full scheduled work shift. Employees
receive a maximum of 5 days of Per Diem each work week while on a job site.
.SP
.B "Travel Time" 
\- Employees are paid for travel time directed by the employer in excess of a normal commute. Traveling long distances
is often required given our customer locations and the areas we service. Travel Time will be automatically calculated based on the
number of miles employee drives (zip code to zip code), assuming an average speed of 60mph. For example, if employee
leaves home driving to a project and the number of miles from home zip code to project zip code is 600 miles, then drive time is
computed as 10 hours. If employee leaves one project headed to another project, then it will be computed as number of miles zip
code to zip code between projects. Normal commute is defined as two hours per day and any travel time incurred in excess of two
hours per day is paid if directed by the employer. Travel time will be paid at Minimum Wage based on the state minimum wage the
employee is leaving from. If the state does not have a set minimum wage, then it will default to Federal Minimum Wage of $7.25 per
hour. By signing this document I confirm I have read the Safety Management Guide for Motor Vehicle Operations and will adhere to
all of its policy guidelines.
.SP
.B "Mileage Reimbursement" 
\- Mileage Reimbursement is also paid to employees traveling to a job site (this does not include
returning home from a job site). Mileage traveled to a job site is only paid in excess of 120 miles. For example, if Employee drives
1,500 miles to a job site, then Employee will receive 1,380 of miles to be reimbursed. Mileage Rates are below. Mileage
Reimbursement is only paid after the Employee arrives and works a full shift at the job site.
.SP
.B
Mileage Rates
.SP
Associates \- $0.15 per mile
.SP
Supervisors \- $0.25 per mile
.R
.SP
.B "Weekly Bonus:"
All employees who have perfect attendance for the week will receive an extra $3 per hour bonus on that week's paycheck.
Perfect Attendance is defined as NO absences, NO tardiness, NO leaving work early, NO turning down work, or in any way does not work the entire scheduled shift.
Employees who do not have Perfect Attendance for the week will receive their normal paycheck; however, they will NOT receive the additional Weekly Bonus.
Bonus is calculated as $3 for every hour worked in the store.  Bonus will not include any time spent traveling to a job site or home, training, or any
other time incurred while not working on a project.  Bonus will be subject to all applicable taxes.
.SP
.B "Personal Travel" 
\- Per Diem is paid to cover travel expenses for the full week, it is not expected that employees return home during
a project or between projects if there are multiple projects back-to-back. Any return home trip not directed by the employer is
considered an employee's personal time and will not be paid.
.SP
.B "Damages" 
\- Employees are responsible for all damages to the hotel room and any hotel or other travel related payments not made.
If employee receives Per Diem and does not pay the hotel bill or damages the room, or incurs smoking or other related room
charges, those charges will be withheld from the employee's next paycheck to the extent permitted by law.
.SP
.B "Down Time" 
\- Employees are assigned to specific projects generally ranging from 1 week to 12 weeks. There is often down time
between projects. Employees will not be compensated for down time unless prior written approval is obtained from the CFO. Also,
due to the seasonal nature of the business, there can be significant periods of down time (i.e. >3 months).
.SP
.B "Personal Protective Equipment"
\- Personal Protective Equipment is required for this job.  Steel or
Composite toe shoes must be worn at all times while on a job site.
Hard hat and Impact Gloves are also required for various job
functions.  You must bring proper PPE with you to work each day.  The
Company will provide you with money to purchase PPE as follows: Steel
or Composite toe shoes ($25); Hard Hat ($10); Impact Gloves ($15).
.SP
.B
By signing below, I acknowledge, understand and wish to be employed pursuant to the above Pay Rate, read in
conjunction with the Employee Handbook, which terms may not be modified except without the expressed written
consent of the CFO.
.R
.SP 3
.nf
.TS
tab(~);
Lw(3i) Lw(.7in) Lw(1in).
$SIGNATURE~~$DATE
\_~~\_
$NAME~~Date
.TE
