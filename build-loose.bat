@echo off
REM  This file is part of JATF.
REM
REM  JATF is free software: you can redistribute it and/or modify
REM  it under the terms of the GNU General Public License as published by
REM  the Free Software Foundation, version 3 of the License.
REM
REM  JATF is distributed in the hope that it will be useful,
REM  but WITHOUT ANY WARRANTY; without even the implied warranty of
REM  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM  GNU General Public License for more details.
REM
REM  You should have received a copy of the GNU General Public License
REM  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
REM
mvn clean install process-resources -Pjatf-example-loose
pause
