
export class MovieResponse {
    title: any; 
    actors: any; 
    genre: any; 
    yearOfRelease: any; 
    rating: any; 
    streamLink: any; 
    posterLink: any | null; 
  
    constructor(
      title: any,
      actors: any,
      genre: any,
      yearOfRelease: any,
      rating: any,
      streamLink: any,
      posterLink: any | null = null // Optional with default value
    ) {
      this.title = title;
      this.actors = actors;
      this.genre = genre;
      this.yearOfRelease = yearOfRelease;
      this.rating = rating;
      this.streamLink = streamLink;
      this.posterLink = posterLink;
    }
  }
  