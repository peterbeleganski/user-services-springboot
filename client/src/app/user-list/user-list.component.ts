import { Component, OnInit } from '@angular/core';
import { UserService } from '../shared/user/user.service'
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  user: CreateUserModel = new CreateUserModel();
  users: Array<any>;

  constructor(private userService: UserService, private http: HttpClient) { }

  ngOnInit() {
    this.userService.getAll().subscribe(data => {
      this.users = data;
    })
  }

  createUser(regForm) {
    console.log(regForm.value)
    regForm.value.dateOfBirth = '2015-05-05';
    this.http
      .post("http://localhost:8080/api/users", (regForm.value))
      .toPromise()
      .then(res => {
        console.log(res)
        this.users.push(res);
      });
  }

  deleteUser(email) {
    console.log(email)
    this.http
      .delete("http://localhost:8080/api/users/" + email)
      .toPromise()
      .then(res => {
        this.userService.getAll().subscribe(data => {
          this.users = data;
        })
      })
  }
}

class CreateUserModel {
  constructor(
    public firstName?: string,
    public lastName?: number,
    public email?: string,
    public dateOfBirth?: string,
  ) { }
}
